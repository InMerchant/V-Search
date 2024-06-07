package com.mysite.sbb.agechk;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysite.sbb.subscribe.SubscribeRepository;

import lombok.RequiredArgsConstructor;

@Service
public class VideoChkService {
	
	private final VideoChkRepository videoChkRepository;

    @Autowired
    public VideoChkService(VideoChkRepository videoChkRepository) {
        this.videoChkRepository = videoChkRepository;
    }

    public int calculateVideoAge(Long videoNo) {
        List<Object[]> scriptimoCounts = videoChkRepository.countScriptimoByVideoNo(videoNo);
        List<Object[]> nudeCounts = videoChkRepository.countNudeByVideoNo(videoNo);

        int[] scriptimoCountArray = new int[7];  // scriptimo 카운트를 저장할 배열
        int[] nudeCountArray = new int[3];       // nude 카운트를 저장할 배열

        for (Object[] result : scriptimoCounts) {
            int scriptimo = ((Number) result[0]).intValue();
            long count = (long) result[1];
            if (scriptimo >= 0 && scriptimo <= 6) {
                scriptimoCountArray[scriptimo] = (int) count;
            }
        }

        for (Object[] result : nudeCounts) {
            int nude = ((Number) result[0]).intValue();
            long count = (long) result[1];
            if (nude >= 0 && nude <= 2) {
                nudeCountArray[nude] = (int) count;
            }
        }

        int totalScore = calculateTotalScore(scriptimoCountArray, nudeCountArray);
        return determineAgeCategory(totalScore);
    }

    private int calculateTotalScore(int[] scriptimoCounts, int[] nudeCounts) {
        int totalScore = 0;

        totalScore += scriptimoCounts[0] * 1;  // 0: 비난
        totalScore += scriptimoCounts[1] * 1;  // 1: 차별
        totalScore += scriptimoCounts[2] * 1;  // 2: 혐오
        totalScore += scriptimoCounts[3] * 5;  // 3: 선정
        totalScore += scriptimoCounts[4] * 10; // 4: 욕설
        totalScore += scriptimoCounts[5] * 10; // 5: 폭력
        totalScore += scriptimoCounts[6] * 10; // 6: 범죄

        totalScore += nudeCounts[1] * 20;      // 1: 반누드
        totalScore += nudeCounts[2] * 100;     // 2: 누드

        return totalScore;
    }

    private int determineAgeCategory(int totalScore) {
        if (totalScore == 0) return 0;
        if (totalScore <= 5) return 7;
        if (totalScore <= 20) return 12;
        if (totalScore <= 100) return 15;
        return 18;
    }
}
