package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Tag;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Repository
public class QuestionDtoDaoImpl implements QuestionDtoDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @SuppressWarnings("deprecation")
    public Optional<QuestionDto> getById(Long id) {
        Query query = entityManager.createQuery("select q.id as id," +
                " q.title as title," +
                " q.user.id as authorId," +
                " q.user.fullName as authorName," +
                " q.user.imageLink as authorImage," +
                " q.description as description, " +
                "(select COALESCE(count(*), 0)  from QuestionViewed  where question.id = q.id) as viewCount, " +
                "(select coalesce(sum(r.count),0) from Reputation r where r.author.id = q.user.id) as authorReputation, " +
                "(select count(*) from Answer a where a.question.id = q.id) as countAnswer, " +
                "(select coalesce(sum(case when v.voteTypeQ = 'UP' then 1 when v.voteTypeQ = 'DOWN' then -1 end), 0) from VoteQuestion v where v.question.id = q.id) as countValuable, " +
                "q.persistDateTime as persistDateTime," +
                "q.lastUpdateDateTime as lastUpdateDateTime, " +
                "qt as tags " +
                "from Question q " +
                "join q.tags qt " +
                "where q.id = :id").setParameter("id", id)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new QuestionDtoWithListTagDtoTransformer());
        return SingleResultUtil.getSingleResultOrNull(query);
    }

    private static class QuestionDtoWithListTagDtoTransformer implements ResultTransformer {

        private final Map<Long, List<TagDto>> tagMap = new HashMap<>();

        private final List<QuestionDto> roots = new ArrayList<>();

        @Override
        public Object transformTuple(Object[] tuple, String[] aliaces) {
            long id = (Long) tuple[0];
            String title = (String) tuple[1];
            long authorId = (Long) tuple[2];
            String authorName = (String) tuple[3];
            String authorImage = (String) tuple[4];
            String description = (String) tuple[5];
            long viewCount = (Long) tuple[6];
            long authorReputation = (Long) tuple[7];
            long countAnswer = (Long) tuple[8];
            long countVariable = (Long) tuple[9];
            LocalDateTime persistDateTime = LocalDateTime.parse(tuple[10].toString(), DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime lastUpdateDateTime = LocalDateTime.parse(tuple[11].toString(), DateTimeFormatter.ISO_DATE_TIME);
            Tag tag = (Tag) tuple[12];

            QuestionDto questionDto = new QuestionDto(id, title, authorId, authorName, authorImage, description, viewCount,
                    authorReputation, countAnswer, countVariable, persistDateTime, lastUpdateDateTime, new ArrayList<>());

            if (!tagMap.containsKey(id)) {
                roots.add(questionDto);
                tagMap.put(id, new ArrayList<>());
            }
            tagMap.get(id).add(new TagDto(tag.getId(), tag.getName(), tag.getDescription()));
            return questionDto;
        }

        @Override
        public List transformList(List list) {
            for (QuestionDto questionDto : roots) {
                questionDto.setListTagDto(tagMap.get(questionDto.getId()));
            }
            return roots;
        }
    }
}
