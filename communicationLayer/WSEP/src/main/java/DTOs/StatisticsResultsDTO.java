package DTOs;

import DTOs.SimpleDTOS.DailyStatsDTO;

import java.util.List;

public class StatisticsResultsDTO extends ActionResultDTO {
    List<DailyStatsDTO> stats;

    public StatisticsResultsDTO(ResultCode resultCode, String details, List<DailyStatsDTO> stats) {
        super(resultCode, details);
        this.stats = stats;
    }

    public List<DailyStatsDTO> getStats() {
        return stats;
    }

    public void setStats(List<DailyStatsDTO> stats) {
        this.stats = stats;
    }
}
