package com.hwarrk.repository;

import com.hwarrk.common.constant.PositionType;
import com.hwarrk.common.constant.PostFilterType;
import com.hwarrk.common.constant.SkillType;
import com.hwarrk.common.constant.WayType;
import com.hwarrk.common.dto.dto.PostWithLikeDto;
import java.util.List;

public interface PostRepositoryCustom {

    List<PostWithLikeDto> findFilteredPost(PositionType positionType, WayType wayType, SkillType skillType,
                                           PostFilterType filterType, String keyWord, long memberId);
}
