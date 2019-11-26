package com.ai.redis.auto;

import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReloadConfigThread extends Thread {
    private final Logger log = LoggerFactory.getLogger(ReloadConfigThread.class);
	private AutoPlaceholderConfigurer configurer;

	public ReloadConfigThread(AutoPlaceholderConfigurer autoPlaceholderConfigurer) {
		this.configurer = autoPlaceholderConfigurer;
	}

	private static boolean isStart;

	public synchronized void start0() {
		if (isStart) {
			return;
		}
		isStart = true;
		System.err.println("已开启配置文件自动刷新模式，刷新间隔："
				+ configurer.getReloadInterval() + "ms");
		start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(configurer.getReloadInterval());
				long s = System.currentTimeMillis();
				Properties properties = null;

				if (properties.isEmpty()) {
					continue;
				}
				Map refreshProperties = configurer.refreshProperties(configurer.newResolver(properties));
			} catch (Exception e) {
			    log.error(e.getMessage(),ExceptionUtils.getStackTrace(e));
			}
		}
	}
}
