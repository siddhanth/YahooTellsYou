package test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.data.googleSearch.GoogleApi;

public class ThreadPool {
	private BlockingQueue<Runnable> taskQueue;

	public ThreadPool(int numberOfThreads) {
		taskQueue = new LinkedBlockingQueue<Runnable>(10);

		for (int i = 0; i < numberOfThreads; i++) {
			new PoolThread(taskQueue).start();
		}
	}

	public void execute(Runnable task) throws InterruptedException {
		taskQueue.put(task);
	}

	public static void main(String[] args) throws Exception {
		ThreadPool t = new ThreadPool(5);
		GoogleApi g = new GoogleApi("who killed mahatma gandhi");
		t.execute(g);

	}
}

class PoolThread extends Thread {
	private BlockingQueue<Runnable> taskQueue;

	public PoolThread(BlockingQueue<Runnable> queue) {
		taskQueue = queue;
	}

	public void run() {
		while (true) {
			try {
				Runnable x = taskQueue.take();
				x.run();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}