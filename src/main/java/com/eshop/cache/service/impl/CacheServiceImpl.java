package com.eshop.cache.service.impl;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.eshop.cache.model.ProductInfo;
import com.eshop.cache.service.CacheService;

/**
 * 缓存Service实现类
 * @author Administrator
 *
 */
@Service("cacheService")
public class CacheServiceImpl implements CacheService {
	
	public static final String CACHE_NAME = "local";
	
	/** 
	 * 将商品信息保存到本地缓存中
	 * @param productInfo
	 * @return
	 */
	@CachePut(value = CACHE_NAME, key = "'key_'+#productInfo.getId()")
	@Override
	public ProductInfo saveLocalCache(ProductInfo productInfo) {
		return productInfo;
	}
	
	/**
	 * 从本地缓存中获取商品信息
	 * @param id 
	 * @return
	 */
	@Cacheable(value = CACHE_NAME, key = "'key_'+#id")
	@Override
	public ProductInfo getLocalCache(Long id) {
		return null;
	}
	
}
