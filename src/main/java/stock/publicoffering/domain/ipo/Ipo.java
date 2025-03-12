package stock.publicoffering.domain.ipo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
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
@Table(indexes = {
        @Index(name = "idx_offering_start_date", columnList = "offeringStartDate"),
})
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
    private String detailLink;

    @Comment("기관 경쟁률")
    private String competitionRatingByInstitution;

    @Comment("의무 보유 확약 비율")
    private float mandatoryHoldingRatio;

    public void setCompetitionRateAndHoldingRatio(String competitionRateAndHoldingRatio, float mandatoryHoldingRatio) {
        this.competitionRatingByInstitution = competitionRateAndHoldingRatio;
        this.mandatoryHoldingRatio = mandatoryHoldingRatio;
    }

}