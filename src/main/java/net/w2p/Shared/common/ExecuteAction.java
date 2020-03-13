package net.w2p.Shared.common;

/**
 * 
 * @author: XiaHui
 * @date 2017-12-25 09:43:12
 */
public interface ExecuteAction {

	public <T, E> E execute(T value);
}