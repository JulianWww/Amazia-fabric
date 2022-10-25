package net.denanu.amazia.pathing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import net.denanu.amazia.pathing.interfaces.PathingEventListener;
import net.minecraft.util.math.BlockPos;

public class PathingEventEmiter {
	private Map<Integer, Map<Integer, Map<Integer, Set<PathingEventListener>>>> listeners;
	
	PathingEventEmiter() {
		this.listeners = new HashMap<Integer, Map<Integer, Map<Integer, Set<PathingEventListener>>>>();
	}
	
	public void registerListener(PathingEventListener listener, BlockPos pos) {
		this.setGet(pos).add(listener);
	}
	
	public void removeListener(PathingEventListener listener, BlockPos pos) {
		this.setGet(pos).remove(listener);
	}
	
	public void sendCreate(BlockPos pos) {
		@Nullable
		Set<PathingEventListener> data = this.get(pos);
		if (data != null) {
			for (PathingEventListener listener : data) {
				listener.onCreate(pos);
			}
		}
	}

	public void sendDestroy(BlockPos pos) {
		@Nullable
		Set<PathingEventListener> data = this.get(pos);
		if (data != null) {
			for (PathingEventListener listener : data) {
				listener.onDestroy(pos);
			}
		}
	}
	
	private Map<Integer, Map<Integer, Set<PathingEventListener>>> setGetX(int idx) {
		if (this.listeners.containsKey(idx)) {
			return this.listeners.get(idx);
		}
		Map<Integer, Map<Integer, Set<PathingEventListener>>> out = new HashMap<Integer, Map<Integer, Set<PathingEventListener>>>();
		this.listeners.put(idx, out);
		return out;
	}
	private static Map<Integer, Set<PathingEventListener>> setGetY(int idx, Map<Integer, Map<Integer, Set<PathingEventListener>>> data) {
		if (data.containsKey(idx)) {
			return data.get(idx);
		}
		Map<Integer, Set<PathingEventListener>> out = new HashMap<Integer, Set<PathingEventListener>>();
		data.put(idx, out);
		return out;
	}
	private static Set<PathingEventListener> setGetZ(int idx, Map<Integer, Set<PathingEventListener>> data) {
		if (data.containsKey(idx)) {
			return data.get(idx);
		}
		Set<PathingEventListener> out = new HashSet<PathingEventListener>();
		data.put(idx, out);
		return out;
	}
	private Set<PathingEventListener> setGet(int x, int y, int z) {
		return setGetZ(z, setGetY(y, this.setGetX(x)));
	}
	private Set<PathingEventListener> setGet(BlockPos pos) {
		return this.setGet(pos.getX(), pos.getY(), pos.getZ());
	} 
	
	@Nullable
	private Map<Integer, Map<Integer, Set<PathingEventListener>>> getX(int idx) {
		if (this.listeners.containsKey(idx)) {
			return this.listeners.get(idx);
		}
		return null;
	}
	@Nullable
	private static Map<Integer, Set<PathingEventListener>> getY(int idx, @Nullable Map<Integer, Map<Integer, Set<PathingEventListener>>> data) {
		if (data != null && data.containsKey(idx)) {
			return data.get(idx);
		}
		return null;
	}
	@Nullable
	private static Set<PathingEventListener> getZ(int idx, Map<Integer, @Nullable Set<PathingEventListener>> data) {
		if (data != null && data.containsKey(idx)) {
			return data.get(idx);
		}
		return null;
	}
	@Nullable
	private Set<PathingEventListener> get(int x, int y, int z) {
		return getZ(z, getY(y, this.getX(x)));
	}
	@Nullable
	private Set<PathingEventListener> get(BlockPos pos) {
		return this.get(pos.getX(), pos.getY(), pos.getZ());
	} 
}