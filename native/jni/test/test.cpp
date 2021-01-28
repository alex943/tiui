#include <ostream>
#include <iostream>
#include <sstream>
#include <ostream>
#include <string>

void Msg() {
    std::ostringstream msg;
    return msg;
}

void LOG(std::ostringstream os) {
    std::cout << os.str() << std::endl;
}

int main() {
    std::ostringstream msg = Msg() << "a" << std::endl;
    return 0;
}