import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    default List<Review> findReviewsByProduct(int productId) {
        return this.findAll()
                .stream()
                .filter(rev -> rev.getProductId() == productId)
                .collect(Collectors.toList());
    }
}
