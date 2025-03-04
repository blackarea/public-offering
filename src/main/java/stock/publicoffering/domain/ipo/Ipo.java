package stock.publicoffering.domain.ipo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Ipo {

    @Id
    @Comment("공모주명")
    private String ipoName;

    @Comment("공모 시작일")
    private LocalDate offeringStartDate;

    @Comment("공모 종료일")
    private LocalDate offeringEndDate;

    @Comment("확정공모가")
    private String finalPrice;

    @Comment("희망공모가")
    private String expectedPrice;

    @Comment("경쟁률")
    private String competitionRate;

    @Comment("주간사")
    private String company;

    @Comment("공모주 세부 링크")
    private String link;
}