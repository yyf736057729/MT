package org.spring.springboot.config;
 
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.annotation.Id;
 
/**
 * 该 pojo 类主要为每个集合记录自增的序列
 * 
 * @author Vijay
 *
 */
@Document(collection = "sequence")
public class SequenceId {
	@Id
	private String Id;			//主键
	@Field("collName")
	private String collName;	//集合名称
	@Field("seqId")
	private long seqId;			//序列值
 
	// getter() setter() 省略


	public String getCollName() {
		return collName;
	}

	public void setCollName(String collName) {
		this.collName = collName;
	}

	public long getSeqId() {
		return seqId;
	}

	public void setSeqId(long seqId) {
		this.seqId = seqId;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}
}
