
package com.gihub.surpassm.iot.mqtt.service.impl;

import com.gihub.surpassm.iot.mqtt.pojo.DupPubRelMessageStore;
import com.gihub.surpassm.iot.mqtt.service.IDupPubRelMessageStoreService;
import com.gihub.surpassm.iot.mqtt.service.IMessageIdService;
import org.apache.ignite.IgniteCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DupPubRelMessageStoreService implements IDupPubRelMessageStoreService {

	@Resource
	private IMessageIdService messageIdService;

	@Resource
	private IgniteCache<String, ConcurrentHashMap<Integer, DupPubRelMessageStore>> dupPubRelMessageCache;

	@Override
	public void put(String clientId, DupPubRelMessageStore dupPubRelMessageStore) {
		ConcurrentHashMap<Integer, DupPubRelMessageStore> map = dupPubRelMessageCache.containsKey(clientId) ? dupPubRelMessageCache.get(clientId) : new ConcurrentHashMap<Integer, DupPubRelMessageStore>();
		map.put(dupPubRelMessageStore.getMessageId(), dupPubRelMessageStore);
		dupPubRelMessageCache.put(clientId, map);
	}

	@Override
	public List<DupPubRelMessageStore> get(String clientId) {
		if (dupPubRelMessageCache.containsKey(clientId)) {
			ConcurrentHashMap<Integer, DupPubRelMessageStore> map = dupPubRelMessageCache.get(clientId);
			Collection<DupPubRelMessageStore> collection = map.values();
			return new ArrayList<>(collection);
		}
		return new ArrayList<>();
	}

	@Override
	public void remove(String clientId, int messageId) {
		if (dupPubRelMessageCache.containsKey(clientId)) {
			ConcurrentHashMap<Integer, DupPubRelMessageStore> map = dupPubRelMessageCache.get(clientId);
			if (map.containsKey(messageId)) {
				map.remove(messageId);
				if (map.size() > 0) {
					dupPubRelMessageCache.put(clientId, map);
				} else {
					dupPubRelMessageCache.remove(clientId);
				}
			}
		}
	}

	@Override
	public void removeByClient(String clientId) {
		if (dupPubRelMessageCache.containsKey(clientId)) {
			ConcurrentHashMap<Integer, DupPubRelMessageStore> map = dupPubRelMessageCache.get(clientId);
			map.forEach((messageId, dupPubRelMessageStore) -> {
				messageIdService.releaseMessageId(messageId);
			});
			map.clear();
			dupPubRelMessageCache.remove(clientId);
		}
	}
}
