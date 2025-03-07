package stock.publicoffering.domain.ipo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MyIpoCompanyTest {

    @Test
    @DisplayName("내 청약회사 테스트")
    void MyIpoCompanyTest() {
        // given
        String existingCompany = "NH투자증권";
        String nonExistingCompany = "대신증권";

        // when
        boolean isExistingCompany = MyIpoCompany.contains(existingCompany);
        boolean isNonExistingCompany = MyIpoCompany.contains(nonExistingCompany);

        // then
        Assertions.assertThat(isExistingCompany).isTrue();
        Assertions.assertThat(isNonExistingCompany).isFalse();
    }
}
