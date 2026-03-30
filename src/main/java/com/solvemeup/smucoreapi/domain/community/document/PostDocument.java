package com.solvemeup.smucoreapi.domain.community.document;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;

@Document(indexName = "posts")
@Setting(settingPath = "elasticsearch/post-settings.json")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "nori_analyzer", searchAnalyzer = "nori_analyzer")
    private String title;

    @Field(type = FieldType.Text, analyzer = "nori_analyzer", searchAnalyzer = "nori_analyzer")
    private String content;

    @Field(type = FieldType.Keyword)
    private String authorName;

    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    public static PostDocument of(Long id, String title, String content, String authorName, LocalDateTime createdAt) {
        PostDocument doc = new PostDocument();
        doc.id = String.valueOf(id);
        doc.title = title;
        doc.content = content;
        doc.authorName = authorName;
        doc.createdAt = createdAt;
        return doc;
    }
}
