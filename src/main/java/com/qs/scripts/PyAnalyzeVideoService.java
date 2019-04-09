package com.qs.scripts;

import lombok.extern.slf4j.Slf4j;
import org.python.core.Py;
import org.python.core.PyList;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;
import org.springframework.stereotype.Service;

/**
 * python 解析视频
 *
 * @author Fbin
 * @version 2019/4/8 21:52
 */
@Slf4j
@Service
public class PyAnalyzeVideoService {

    /**
     * 执行script
     *
     * @param scriptPath
     */
    public void execute(String scriptPath){
        PySystemState sys = Py.getSystemState();
        PyList pyList = sys.path;
        for(Object obj : pyList){
            log.info(obj.toString());
        }

        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile(scriptPath);
    }

}
