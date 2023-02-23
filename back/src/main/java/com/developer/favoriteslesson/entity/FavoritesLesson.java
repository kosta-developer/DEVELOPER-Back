<<<<<<< HEAD
package com.developer.favoriteslesson.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import com.developer.lesson.entity.Lesson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="FAVORITES_LESSON")
@DynamicInsert

@Setter@Getter @ToString
@NoArgsConstructor @AllArgsConstructor
@SequenceGenerator(
		name ="favLesSeq", 
		sequenceName ="fav_les_seq", 
		initialValue = 1, allocationSize = 1 
		)
public class FavoritesLesson {
	@Id
	@Column(name="fav_les_seq")
	@GeneratedValue( 
			strategy = GenerationType.SEQUENCE, 
			generator ="favLesSeq"  
		)
	private Long favLesSeq;
	
	@Column(name="user_id")
	private String userId;
	
	@ManyToOne()
	@JoinColumn(name="lessonSeq")
	private Lesson lesson;
}
=======
package com.developer.favoriteslesson.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import com.developer.lesson.entity.Lesson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="FAVORITES_LESSON")
@DynamicInsert

@Setter@Getter @ToString
@NoArgsConstructor @AllArgsConstructor
@SequenceGenerator(
		name ="favLesSeq", // 사용할 sequence 이름
		sequenceName ="fav_les_seq", // 실제 데이터베이스 sequence 이름
		initialValue = 1, allocationSize = 1 
		)
public class FavoritesLesson {
	@Id
	@Column(name="fav_les_seq")
	@GeneratedValue( 
			strategy = GenerationType.SEQUENCE, 
			generator ="favLesSeq" // 위의 sequence 이름 
		)
	private Long favLesSeq;
	
	@ManyToOne
	@JoinColumn(name="fl_lessonSeq")
	private Lesson lesson;
}
>>>>>>> 27530127c719d1d04269250f27e3d6eb6704271f
