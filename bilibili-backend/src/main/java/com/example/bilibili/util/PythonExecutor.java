package com.example.bilibili.util;

import org.springframework.stereotype.Component;
import java.io.*;
import java.util.concurrent.*;

@Component
public class PythonExecutor {
    private static final int TIMEOUT = 30;

    public static String executePythonScript(String scriptPath, String... args) {
        Process process = null;
        StringBuilder output = new StringBuilder();
        StringBuilder errorOutput = new StringBuilder();
        try {
            String pythonCmd = System.getProperty("os.name").toLowerCase().contains("win")
                    ? "python" : "python3";

            String scriptAbsolutePath = new File("src/main/resources/" + scriptPath).getAbsolutePath();

            String[] cmd = new String[args.length + 2];
            cmd[0] = pythonCmd;
            cmd[1] = scriptAbsolutePath;
            System.arraycopy(args, 0, cmd, 2, args.length);

            ProcessBuilder pb = new ProcessBuilder(cmd)
                    .redirectErrorStream(false) // 分开捕获错误流
                    .directory(new File(System.getProperty("user.dir")));

            process = pb.start();

            // 读取标准输出
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            // 读取错误输出
            try (BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorOutput.append(line).append("\n");
                }
            }

            if (!process.waitFor(TIMEOUT, TimeUnit.SECONDS) || process.exitValue() != 0) {
                throw new RuntimeException("Python脚本执行失败，错误信息: " + errorOutput.toString());
            }

            return output.toString();
        } catch (Exception e) {
            throw new RuntimeException("执行错误: " + e.getMessage());
        } finally {
            if (process != null) {
                process.destroyForcibly();
            }
        }
    }

    private static String readAllLines(BufferedReader reader) throws IOException {
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line);
        }
        return output.toString();
    }
}