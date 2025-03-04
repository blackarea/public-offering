package stock.publicoffering.domain.ipo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpoRepository extends JpaRepository<Ipo, String> {

}
