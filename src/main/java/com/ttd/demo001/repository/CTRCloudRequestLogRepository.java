package com.ttd.demo001.repository;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ttd.demo001.entity.CTRCloudRequestLog;

@Repository
@Transactional
public class CTRCloudRequestLogRepository implements CrudRepository<CTRCloudRequestLog, Long> {

	@Autowired
	private EntityManager entityManager;

	@Override
	public <S extends CTRCloudRequestLog> S save(S entity) {
		entityManager.persist(entity);
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends CTRCloudRequestLog> Iterable<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<CTRCloudRequestLog> findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existsById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<CTRCloudRequestLog> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<CTRCloudRequestLog> findAllById(Iterable<Long> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(CTRCloudRequestLog entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll(Iterable<? extends CTRCloudRequestLog> entities) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub

	}

}
