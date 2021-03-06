package sopt.org.moca.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import sopt.org.moca.dto.*;

import java.util.List;


@Mapper
public interface CafeMapper {

    /**
     *
     *
     *     검증 카페
     *
     *
     */

    //인기 있는 검증 카페 리스트 조회 (갯수)
    @Select("select EVALUATED_CAFE.cafe_id, cafe_name , address_district_name, evaluated_cafe_img_url, evaluated_cafe_rating , " +
            "(CASE WHEN EVALUATED_CAFE.cafe_id in (select cafe_id from SCRAP where user_id = #{user_id} )THEN 1 ELSE 0 END) as scrab_is " +
            "            from EVALUATED_CAFE  natural join CAFE inner  join ADDRESS_DISTRICT on CAFE.cafe_address_district_id = ADDRESS_DISTRICT.address_district_id left join EVALUATED_CAFE_IMG on EVALUATED_CAFE.cafe_id = EVALUATED_CAFE_IMG.cafe_id  \n" +
            "            where (evaluated_cafe_main_img = 1 or ISNULL(evaluated_cafe_main_img))\n" +
            "            ORDER BY evaluated_cafe_rating DESC \n" +
            "            limit  #{length} " )
    List<EvaluatedCafeSimple>findPopularEvaluatedCafe(@Param("length") final int length, @Param("user_id")final String user_id);

    //인기 있는 검증 카페 리스트 조회(전체)
    @Select("select EVALUATED_CAFE.cafe_id, cafe_name , address_district_name, evaluated_cafe_img_url, evaluated_cafe_rating, " +
            "(CASE WHEN EVALUATED_CAFE.cafe_id in (select cafe_id from SCRAP where user_id = #{user_id} )THEN 1 ELSE 0 END) as scrab_is " +
            "            from EVALUATED_CAFE  natural join CAFE inner  join ADDRESS_DISTRICT on CAFE.cafe_address_district_id = ADDRESS_DISTRICT.address_district_id left join EVALUATED_CAFE_IMG on EVALUATED_CAFE.cafe_id = EVALUATED_CAFE_IMG.cafe_id  \n" +
            "            where (evaluated_cafe_main_img = 1 or ISNULL(evaluated_cafe_main_img))\n" +
            "            ORDER BY evaluated_cafe_rating DESC \n")
    List<EvaluatedCafeSimple>findAllEvaluatedCafe(@Param("user_id")final String user_id);


    //검증 카페 상세 정보 조회(카페이름, 카페주소, 총평, 평균 별점)
    @Select("select cafe_name,cafe_address_detail , evaluated_cafe_total_evaluation, evaluated_cafe_rating " +
            "from EVALUATED_CAFE natural join CAFE " +
            "where cafe_id = #{cafe_id}")
    EvaluatedCafeInfo findEvaluatedCafeInfo(@Param("cafe_id")final int cafe_id);



    //검증 카페  이미지 조회
    @Select("select evaluated_cafe_img_url,evaluated_cafe_main_img " +
            "from EVALUATED_CAFE_IMG " +
            "where cafe_id = #{cafe_id}")
    List<EvaluatedCafeImg>findEvaluatedCafeImg(@Param("cafe_id")final int cafe_id);




    //검증 카페 검증 위원 평가 정보 리스트 조회
    @Select("select barista_id, barista_name, barista_title, barista_img_url, " +
            "evaluation_bean_condition, evaluation_bean_condition_comment, evaluation_roasting, evaluation_roasting_comment, evaluation_creativity, evaluation_creativity_comment, evaluation_reasonable, evaluation_reasonable_comment, evaluation_consistancy, evaluation_consistancy_comment " +
            "from EVALUATION natural join BARISTA " +
            "where cafe_id = #{cafe_id}")
    List<Evaluation>findAllEvaluation(@Param("cafe_id")final int cafe_id);


    //해당 검증 위원 평가 정보 조회
    @Select("select barista_name,barista_title,barista_img_url,evaluation_bean_condition,evaluation_bean_condition_comment,evaluation_roasting,evaluation_roasting_comment,evaluation_creativity,evaluation_creativity_comment,evaluation_reasonable,evaluation_reasonable_comment,evaluation_consistancy,evaluation_consistancy_comment,evaluation_summary " +
            "from EVALUATION natural join BARISTA " +
            "where barista_id = #{barista_id} AND cafe_id = #{cafe_id}")
    Evaluation_detail findBaristaEvaluation(@Param("cafe_id")final int cafe_id,@Param("barista_id")final int barista_id);

    /**
     *
     *
     *카페 상세보기
     *
     *
     *
     */

    // 카페 간단 조회 (REVIEW에서 사용)
    @Select("SELECT CAFE.cafe_id, CAFE.cafe_name, ADDRESS_DISTRICT.address_district_name " +
            "FROM CAFE, ADDRESS_DISTRICT " +
            "WHERE CAFE.cafe_id = #{cafe_id} " +
            "AND ADDRESS_DISTRICT.address_district_id = CAFE.cafe_address_district_id")
    CafeInfo findByCafeId(@Param("cafe_id") final int cafe_id);


    //카페 이미지 리스트 조회
    @Select("select  cafe_img_url, cafe_img_main from CAFE_IMG where cafe_id = #{cafe_id} order by cafe_img_main DESC")
    List<CafeImg> findCafeImgList(@Param("cafe_id")final int cafe_id);


//    @Select("select cafe_img_url from CAFE_IMG where cafe_id = #{cafe_id}")
//    String findCafeImg(@Param("cafe_id") final int cafe_id);


    //카페 상세 정보 조회  model
    @Select("select cafe_name, cafe_latitude, cafe_longitude, cafe_phone, cafe_menu_img_url, address_district_name, cafe_address_detail, " +
            "cafe_rating_avg, cafe_times, cafe_days, cafe_option_parking, cafe_option_wifi, cafe_option_allnight, cafe_option_reservation, cafe_option_smokingarea, " +
            " (CASE  WHEN CAFE.cafe_id  in (select cafe_id from SCRAP where user_id = #{user_id})THEN 1 ELSE 0 END) as cafe_scrab_is " +
            "from (CAFE LEFT join CAFE_OPTION on CAFE.cafe_id = CAFE_OPTION.cafe_id) join ADDRESS_DISTRICT on cafe_address_district_id = address_district_id " +
            "where CAFE.cafe_id = #{cafe_id}")
    CafeInfo findCafeInfo(@Param("cafe_id")final int cafe_id ,@Param("user_id")final String user_id);


    //시그니처 메뉴 리스트 조회
    @Select("select cafe_signiture_menu,cafe_signiture_price,cafe_signiture_img " +
            "from CAFE_SIGNITURE " +
            "where cafe_id = #{cafe_id}")
    List<CafeSignitureMenu> findCafeSigitureMenuList(@Param("cafe_id")final int cafe_id);


    /**
     * 카페 카테고리
     */

    //해당 지역구 전체
    @Select("select cafe_id, cafe_name, cafe_introduction, cafe_latitude, cafe_longitude ,cafe_rating_avg,cafe_img_url " +
            "from CAFE natural join CAFE_IMG " +
            "where cafe_img_main = 1 and cafe_address_district_id = #{address_district_id}")
    List<CafeSimple> findCafeInfoList(@Param("address_district_id")final int address_district_id);





    // 인기 카페 리스트 조회 (리뷰개수 순)
    @Select("SELECT C.cafe_id, C.cafe_name, CI.cafe_img_url, C.review_count " +
            "FROM (SELECT CAFE.cafe_id, CAFE.cafe_name, R.review_count FROM CAFE " +
            "JOIN (SELECT cafe_id, COUNT(*) AS review_count FROM REVIEW GROUP BY cafe_id) as R " +
            "ON R.cafe_id = CAFE.cafe_id) as C LEFT JOIN " +
            "(SELECT * FROM CAFE_IMG WHERE CAFE_IMG.cafe_img_main = 1) as CI " +
            "ON C.cafe_id = CI.cafe_id ORDER BY review_count DESC LIMIT 5;")
    List<CafeBest>findBestCafeOrderByReviewCnt();




    // 인기 카페 리스트 조회 (스크랩 순)
    @Select("SELECT C.cafe_id, C.cafe_name, CI.cafe_img_url, C.scrap_count " +
            "FROM (SELECT CAFE.cafe_id, CAFE.cafe_name, R.scrap_count FROM CAFE " +
            "JOIN (SELECT cafe_id, COUNT(*) AS scrap_count FROM SCRAP GROUP BY cafe_id) as R " +
            "ON R.cafe_id = CAFE.cafe_id) as C LEFT JOIN " +
            "(SELECT * FROM CAFE_IMG WHERE CAFE_IMG.cafe_img_main = 1) as CI " +
            "ON C.cafe_id = CI.cafe_id ORDER BY scrap_count DESC LIMIT 5;")
    List<CafeBest>findBestCafeOrderByScrapCnt();


    //핫플레이스 별 카페리스트 조회
//    @Select("SELECT CAFE.cafe_id, cafe_name , cafe_subway, cafe_rating_avg ,cafe_img_url,(CASE  WHEN CAFE.cafe_id in (select cafe_id from EVALUATED_CAFE )THEN 1 ELSE 0 END) as  is_evaluated_cafe " +
//            "from CAFE left join CAFE_IMG on CAFE.cafe_id = CAFE_IMG.cafe_id " +
//            "where hot_place_id = #{hot_place_id} and (cafe_img_main = 1 or ISNULL(cafe_img_main))")


    /**
     * 1/10 일 수정됨 img_url 제거
     * @param hot_place_id
     * @return
     */

    @Select("SELECT CAFE.cafe_id, cafe_name , cafe_subway, cafe_rating_avg ," +
            "(CASE  WHEN CAFE.cafe_id in (select cafe_id from EVALUATED_CAFE )THEN 1 ELSE 0 END) as  evaluated_cafe_is " +
            "from CAFE where hot_place_id = #{hot_place_id}")
    List<CafeByHotPlace>findCafeByHotPlaceList(@Param("hot_place_id")final int hot_place_id);




    //카페 리뷰 별점 랭킹 30개
    @Select("select  CAFE.cafe_id, cafe_name, cafe_menu_img_url, address_district_name , cafe_rating_avg , (CASE  WHEN CAFE.cafe_id in (select cafe_id from EVALUATED_CAFE )THEN 1 ELSE 0 END) as  evaluated_cafe_is " +
            "from CAFE inner join ADDRESS_DISTRICT on CAFE.cafe_address_district_id = ADDRESS_DISTRICT.address_district_id left join CAFE_IMG on CAFE.cafe_id = CAFE_IMG.cafe_id " +
            "where  cafe_img_main = 1 or ISNULL(cafe_img_main) " +
            "order by  cafe_rating_avg  DESC  limit #{length}")
    List<CafeRankingInfo>findCafeListByRanking(@Param("length")final int length);




}