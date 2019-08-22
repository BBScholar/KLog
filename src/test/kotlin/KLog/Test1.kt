package KLog

import io.bbscholar.klog.Loggable
import io.bbscholar.klog.Logger
import io.bbscholar.klog.annotations.Log
import kotlin.test.Test

class TestClass : Loggable {

    @Log("yeet1")
    var yeet1 = 1

    @Log("yeet2")
    var yeet2 = 2

    @Log("yeet3")
    var yeet3 = 3
}

class Test1 {

    @Test fun test1() {
        val temp =  TestClass()
        Logger.register(temp)

    }

}