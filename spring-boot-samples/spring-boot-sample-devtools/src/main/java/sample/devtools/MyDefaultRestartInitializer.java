package sample.devtools;

import java.net.URL;

import org.springframework.boot.devtools.restart.DefaultRestartInitializer;

public class MyDefaultRestartInitializer extends DefaultRestartInitializer {


    @Override
    public URL[] getInitialUrls(Thread thread) {
        if (!isMain(thread)) {
            return null;
        }
        for (StackTraceElement element : thread.getStackTrace()) {
            if (isSkippedStackElement(element)) {
                return null;
            }
        }
        return getUrls(thread);
    }

    /**
     * Returns if the thread is for a main invocation. By default checks the name of the
     * thread and the context classloader.
     *
     * @param thread
     *         the thread to check
     * @return {@code true} if the thread is a main invocation
     */
    protected boolean isMain(Thread thread) {
        final String classLoader = thread.getContextClassLoader().getClass().getName();
        return thread.getName().equals("main") && (classLoader.contains("AppClassLoader") || classLoader.contains("URLClassLoader"));
    }

}

