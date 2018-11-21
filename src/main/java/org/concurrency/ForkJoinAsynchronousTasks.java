package org.concurrency;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ForkJoinAsynchronousTasks {
    public static final Logger LOGGER = Logger.getLogger("CONCURRENCY");

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        final String ext = "java";
        final String bookServicePath = "/Users/rgavrysh/Java/spring/spring-cloud/book-service";
        final String gatewayPath = "/Users/rgavrysh/Java/spring/spring-cloud/gateway";

        FolderProcessor bkService = new FolderProcessor(bookServicePath, ext);
        FolderProcessor gateway = new FolderProcessor(gatewayPath, ext);

        pool.execute(bkService);
        pool.execute(gateway);

        do {
            System.out.println("*************************");
            System.out.printf("Pool\tasync mode\t\t%s\n", pool.getAsyncMode());
            System.out.printf("Pool\tactive threads\t\t%d\n", pool.getActiveThreadCount());
            System.out.printf("Pool\tparallelism\t\t%d\n", pool.getParallelism());
            System.out.printf("Pool\tqueued tasks\t\t%d\n", pool.getQueuedTaskCount());
            System.out.println("*************************");
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                LOGGER.severe("Interrupted!");
                Thread.currentThread().interrupt();
            }
        } while ((!bkService.isDone()) || (!gateway.isDone()));

        List<String> bkServiceJavaFiles = bkService.join();
        List<String> gatewayJavaFiles = gateway.join();

        LOGGER.info(() -> "Files with " + ext + " extension found in " + bookServicePath + "\t\t:" + bkServiceJavaFiles.size());
        LOGGER.info(() -> "Files with " + ext + " extension found in " + gatewayPath + "\t\t:" + gatewayJavaFiles.size());
    }
}

class FolderProcessor extends RecursiveTask<List<String>> {

    private static final long serialVersionUID = 1L;

    private final String path;
    private final String extention;

    public FolderProcessor(String path, String extension) {
        this.path = path;
        this.extention = extension;
    }

    @Override
    protected List<String> compute() {

        List<String> listOfFiles = new ArrayList<>();
        List<FolderProcessor> folderTasks = new ArrayList<>();

        File folder = new File(path);
        File[] files = folder.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    FolderProcessor subfolderTask = new FolderProcessor(files[i].getAbsolutePath(), extention);
                    subfolderTask.fork();
                    folderTasks.add(subfolderTask);
                } else {
                    if (checkFile(files[i].getName())) {
                        listOfFiles.add(files[i].getAbsolutePath());
                    }
                }
            }
            if (folderTasks.size() > 50) {
                ForkJoinAsynchronousTasks.LOGGER.info(() -> folder.getAbsolutePath() + " : " +
                        folderTasks.size() + " tasks ran.");
            }
            addResultsFromTasks(listOfFiles, folderTasks);
        }
        return listOfFiles;
    }

    private boolean checkFile(String name) {
        return name.endsWith(extention);
    }

    private void addResultsFromTasks(List<String> listOfFiles, List<FolderProcessor> folderTasks) {
        for (FolderProcessor item :
                folderTasks) {
            listOfFiles.addAll(item.join());
        }
    }
}
