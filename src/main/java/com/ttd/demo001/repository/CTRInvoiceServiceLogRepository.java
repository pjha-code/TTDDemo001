package com.ttd.demo001.repository;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ttd.demo001.entity.CTRInvoiceServiceLog;

@Repository
@Transactional
public class CTRInvoiceServiceLogRepository implements CrudRepository<CTRInvoiceServiceLog, Long> {

	@Autowired
	EntityManager entityManager;

	@Override
	public <S extends CTRInvoiceServiceLog> S save(S entity) {
		entityManager.persist(entity);
		return null;
	}

	@Override
	public <S extends CTRInvoiceServiceLog> Iterable<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<CTRInvoiceServiceLog> findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existsById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<CTRInvoiceServiceLog> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<CTRInvoiceServiceLog> findAllById(Iterable<Long> ids) {
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
	public void delete(CTRInvoiceServiceLog entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll(Iterable<? extends CTRInvoiceServiceLog> entities) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub

	}

}
