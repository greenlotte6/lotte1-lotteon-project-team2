package kr.co.lotteon.service.user;


import kr.co.lotteon.entity.config.Terms;
import kr.co.lotteon.repository.config.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TermsService {

    private final TermsRepository termsRepository;

    public Terms getTerms () {
        return termsRepository.findById(1).orElse(null);
    }
}
