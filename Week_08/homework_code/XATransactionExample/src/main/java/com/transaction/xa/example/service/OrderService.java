package com.transaction.xa.example.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @ClassName OrderService
 * @Description order service
 * @Author zhangwei
 * @Date 2020-12-03 13:02
 * @Version 1.0
 */
@Service
@Slf4j
public class OrderService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderService(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Execute XA.
     *
     * @param count insert record count
     * @return transaction type
     */
    @Transactional
    @ShardingTransactionType(TransactionType.XA)
    public TransactionType insert(final int count) {
        return jdbcTemplate.execute("INSERT INTO t_order (order_id, user_id, status) VALUES (?, ?, ?)", (PreparedStatementCallback<TransactionType>) preparedStatement -> {
            doInsert(count, preparedStatement);
            return TransactionTypeHolder.get();
        });
    }

    /**
     * Execute XA with exception.
     *
     * @param count insert record count
     */
    @Transactional
    @ShardingTransactionType(TransactionType.XA)
    public void insertFailed(final int count) {
        jdbcTemplate.execute("INSERT INTO t_order (order_id, user_id, status) VALUES (?, ?, ?)", (PreparedStatementCallback<TransactionType>) preparedStatement -> {
            doInsert(count, preparedStatement);
            throw new SQLException("mock transaction failed");
        });
    }

    private void doInsert(final int count, final PreparedStatement preparedStatement) throws SQLException {
        for (int i = 0; i < count; i++) {
            preparedStatement.setObject(1, i);
            preparedStatement.setObject(2, i);
            preparedStatement.setObject(3, "init");
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Select all.
     *
     * @return record count
     */
    public int selectAll() {
        return jdbcTemplate.queryForObject("SELECT COUNT(1) AS count FROM t_order", Integer.class);
    }
}
