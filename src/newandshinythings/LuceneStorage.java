package newandshinythings;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

public class LuceneStorage {

	private static final Logger LOG = Logger.getLogger(LuceneStorage.class.getName());

	private final Directory db;
	private final IndexWriter writer;

	public LuceneStorage(Directory db) {
		this.db = db;
		try {
			writer = new IndexWriter(db,
					new StandardAnalyzer(Version.LUCENE_30),
				                         IndexWriter.MaxFieldLength.LIMITED);
		} catch (CorruptIndexException e) {
			throw new IllegalArgumentException("The index is corrupt!", e);
		} catch (LockObtainFailedException e) {
			throw new IllegalArgumentException("Unable to obtain lock!", e);
		} catch (IOException e) {
			throw new IllegalArgumentException("Unable to read data!", e);
		}
	}

	public void addMyndighet(Myndighet myndighet) {
		Document doc = new Document();
		try {
			Field field = new Field(name, reader);
			doc.add(field);
			writer.addDocument(doc);
		} catch (CorruptIndexException e) {
			throw new IllegalArgumentException("The index is corrupt!", e);
		} catch (IOException e) {
			throw new IllegalArgumentException("Unable to write data!", e);
		}
	}
}
