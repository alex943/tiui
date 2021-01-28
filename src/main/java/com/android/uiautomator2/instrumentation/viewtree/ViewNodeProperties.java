package com.android.uiautomator2.instrumentation.viewtree;

import com.android.tools.apk.analyzer.Archive;
import com.android.tools.apk.analyzer.Archives;
import com.google.devrel.gmscore.tools.apk.arsc.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.List;

public class ViewNodeProperties {

    private Archive archive;
    private byte[] resContents;
    private BinaryResourceFile binaryRes;
    private List<Chunk> chunks;
    private ResourceTableChunk resourceTableChunk;
    private PackageChunk packageChunk;
    private List<TypeChunk> typeChunks;
    private TypeChunk typeChunk;
    private int paxkageId, typeId;
    private String fileSpec, packageSpec;

    private static ViewNodeProperties sViewNodeProperties;

    public static ViewNodeProperties getInstance() {
        if (sViewNodeProperties == null) {
            sViewNodeProperties = new ViewNodeProperties();
        }
        return sViewNodeProperties;
    }

    private ViewNodeProperties() {
        //init();
    }

    public static File getFileSpec(String fileSpec) {
        return new File(fileSpec);
    }
    public static String getTypeSpec() {
        return "id";
    }
    public static String getConfigSpec() {
        return "default";
    }

    //"/Users/ws3/Documents/test/android2/uiautomatorviewer/apk/app-debug.apk",
    //                    "com.example.animations30"
    public void setProperties(String fileSpec, String packageSpec)  {
        this.fileSpec = fileSpec;
        this.packageSpec = packageSpec;
    }

    private void init() {
        try {
            // should have a ZipProvider with <jar> scheme
            for (FileSystemProvider provider: FileSystemProvider.installedProviders()) {
                System.out.println(provider.getScheme());
            }

            System.out.println("000\n" + fileSpec + "\n" + packageSpec);
            Path path = getFileSpec(fileSpec).toPath();
            //archive = new AndroidArtifact(path, createZipFileSystem(fileSpec, true));

            final URI uri = URI.create(path.toUri().getPath());
            //FileSystem fileSystem = FileSystems.newFileSystem(path, ClassLoader.getSystemClassLoader());
//            FileSystem fileSystem = FileSystems.newFileSystem(URI.create("jar:" + uri), new HashMap<>());
//            archive = new AndroidArtifact(path, fileSystem);
//            System.out.println("000 " + path.toString());

            archive = Archives.open(path); // only JDK9 support jar:
            System.out.println("001");

            resContents = Files.readAllBytes(
                    archive.getContentRoot().resolve("resources.arsc"));
            System.out.println("002");
        } catch (IOException e) {
            System.out.println("003");
            System.out.println(e.toString());
            //e.printStackTrace();
            return;
        }

        System.out.println("111");
        binaryRes = new BinaryResourceFile(resContents);
        chunks = binaryRes.getChunks();
        resourceTableChunk = (ResourceTableChunk)chunks.get(0);
        packageChunk = resourceTableChunk.getPackage(packageSpec);
        typeChunks = (List<TypeChunk>) packageChunk.getTypeChunks(getTypeSpec());

        System.out.println("222");
        for (TypeChunk type : typeChunks) {
            if (getConfigSpec().equals(type.getConfiguration().toString())) {
                typeChunk = type;
            }
        }

        System.out.println("333");
        if (typeChunk != null) {
            // [0x packageId typeId index]
            // [0x 7f        08     0129 ]	            wrap_content	false
            paxkageId = packageChunk.getId();
            typeId = typeChunk.getId();
        }
        System.out.println("444");
    }
    /**
     * ApkAnalyzerImpl.resValue
     *
     * @params idName com.android.systemui:id/charge_shadow_effect_layer_e50
     **/
    public synchronized String getId(String idName) {
        System.out.println("idName " + idName);
        if (typeChunk == null || idName == null || idName.isEmpty()) {
            return "";
        }
        if (!idName.contains("/")) {
            return "";
        }
        //-- del : System.out.println("idName " + idName);
        idName = idName.split("/")[1];
        int index = 0;
        for (TypeChunk.Entry entry : typeChunk.getEntries().values()) {
            if (idName.equals(entry.key())) {
                break;
            }
            index++;
        }
        String paxkageIdHex = String.format("%02x", paxkageId);
        String typeIdHex = String.format("%02x", typeId);
        String indexHex = String.format("%04x", index);
        return paxkageIdHex + typeIdHex + indexHex;
    }

}
