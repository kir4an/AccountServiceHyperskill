package account.repository;


import account.model.InternTask;
import account.service.InternService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolutionRepository extends JpaRepository<InternTask,Integer> {
    boolean existsByTaskIgnoreCase(String task);
    String findSolutionByTask(String task);
}
