package com.android.uiautomator2.instrumentation;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.ddmlib.logcat.*;
import com.android.uiautomator2.Logger;
import com.android.uiautomator2.instrumentation.arch.LogcatHandler;
import com.android.uiautomator2.instrumentation.model.Event;
import com.android.uiautomator2.instrumentation.page.Page;
import com.android.uiautomator2.instrumentation.viewtree.ViewTreeImpl;
import com.google.gson.Gson;

import javax.annotation.Nullable;
import java.util.List;

public class TiRuntime {
    public static final String TAG = TiRuntime.class.getSimpleName();
    public static TiRuntime instance;

    private LogCatReceiverTask task;
    private Gson gs = new Gson();
    private ViewTreeImpl mViewTree = new ViewTreeImpl();
    private Page mPage = new Page();

    private LogCatTimestamp startRecord;
    public boolean created = false;
    public boolean stopped = false;

    public static TiRuntime getInstance() {
        if (instance == null) {
            instance = new TiRuntime();
        }
        return instance;
    }

    public ViewTreeImpl getViewTree() {
        return mViewTree;
    }

    public Page getPage() {
        return mPage;
    }

    public void addObserver(TiRuntimeObserver observer) {
        mViewTree.addObserver(observer);
        mPage.addObserver(observer);
    }

    private LogCatListener mLogCatListener = new LogCatListener() {
        private LogCatFilter filters = new LogCatFilter(
                "", "UI_JVMTI", "", "", "", Log.LogLevel.ERROR);

        @Nullable
        private Event build(LogCatMessage msg) {
            Event event2 = null;
            try {
                event2 = gs.fromJson(msg.getMessage(), Event.class);
                event2.setTime(msg.getHeader().getTimestamp().toString());
            } catch (com.google.gson.JsonSyntaxException e) {
                Logger.d("Exception with " + msg.getMessage());
            }
            return event2;
        }

        public Event isLogcatMatched(LogCatMessage msg) {
            if (startRecord == null ||
                    !startRecord.isBefore(msg.getTimestamp())) {
                return null;
            }
            if (!filters.matches(msg)) {
                return null;
            }
            return build(msg);
        }

        public boolean isEventMatched(Event event) {
            if (event == null) return false;
            return !"getId".equals(event.getMethodName())
                    && !"<init>".equals(event.getMethodName());
        }

        public boolean handleView(Event event) {
            if (event.getEventType() == Event.VIEW) {
                mViewTree.add(event);
                return true;
            }
            return false;
        }

        // when finished parsed, calling mTreeViewer.setInput(input);
        public boolean handlePage(Event event) {
            if (event.getEventType() == Event.PAGE) {
                mPage.add(event);
                return true;
            }
            return false;
        }

        @Override
        public void log(List<LogCatMessage> list) {
            for (LogCatMessage msg : list) {
                Event event = isLogcatMatched(msg);
                if (!isEventMatched(event)) {
                    continue;
                }
                if (event.getClassName() != null) {
                    if (event.getMethodName().contains("onCreate")) {
                        // start to record
                        event.setEventType(Event.PAGE);
                        created = true;
                        stopped = false;
                    } else if (event.getMethodName().contains("onDestroy")) {
                        // end to record
                        event.setEventType(Event.PAGE);
                        created = false;
                        stopped = true;
                        mViewTree.clear();
                    } else if (event.getMethodName().contains("onMeasure") ||
                            event.getMethodName().contains("performMeasure")){
                        event.setEventType(Event.VIEW);
                    } else {
                        event.setEventType(Event.PAGE);
                    }
                }

                if (created) {
                    if (handleView(event)) {
                        //Logger.d(TAG + " view " + event.toString());
                    } else if (handlePage(event)) {
                        Logger.d(TAG + " page " + event.toString());
                    }
                }
            }
        }
    };

    private void startInternal(IDevice device) {
        if (task != null) {
            return;
        }
        startRecord = LogcatHandler.currentTime();
        task = new LogCatReceiverTask(device);
        task.addLogCatListener(mLogCatListener);
        new Thread(task).start();
    }

    public void start(IDevice device)  {
        startInternal(device);
    }

    public void stop() {
        created = false;
        stopped = true;
        if (task != null) {
            task.stop();
        }
    }

}
