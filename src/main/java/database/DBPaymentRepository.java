package database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBPaymentRepository extends JpaRepository<DBPayment, Long> {
}