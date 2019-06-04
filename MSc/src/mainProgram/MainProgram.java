package mainProgram;

import java.io.IOException;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

public class MainProgram implements AutoCloseable{
	
	//Neo4j Driver, used to create a session that can execute Cypher queries
		private static Driver driver;

		//Create a new Neo4jClient. Initialises the Neo4j Driver.
		public MainProgram() {
			//Create the Neo4j driver
			driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic("neo4j", "********"));
			// napier password removed for security
		}

		@Override
		public void close() throws Exception
		{
			driver.close();
		}		 


	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub		
		
		new OutputCSV();
		new RotateCSV();
		new Keyword();
		new Cite();
		new CitedAuthPaper();
		
		MainProgram file = new MainProgram();
		
		//load author/title csv
		try (Session session = driver.session()){
			session.run("USING PERIODIC COMMIT " + "LOAD CSV WITH HEADERS FROM 'file:/inputAuthTitle.csv' AS line WITH line, (line.Published_Year) AS year " +
					"MERGE (paper:Paper {name: trim(toLower(line.Paper)),year: (line.Published_Year)})" +
					"MERGE (author:Author {name: trim(toLower(line.Author))})" + "MERGE (author)-[:WROTE]->(paper);");
		} catch (Exception e){
			e.printStackTrace();
		}
		
		//load title/cites csv
		try (Session session = driver.session()){
			session.run("USING PERIODIC COMMIT " + "LOAD CSV WITH HEADERS FROM 'file:/inputCite.csv' AS line " + 
		"MERGE (paper:Paper {name: trim(toLower(line.Paper))})" + 
					"MERGE (cites:Cites {name: trim(toLower(line.Cites))})" + "MERGE (cites)-[:CITED_BY]->(paper);");
		} catch (Exception e){
			e.printStackTrace();
		} 
		
		//load title/keywords csv
		try (Session session = driver.session()){
			session.run("USING PERIODIC COMMIT " + "LOAD CSV WITH HEADERS FROM 'file:/inputKey.csv' AS line " + 
		"MERGE (paper:Paper {name: trim(toLower(line.Paper))})" + 
					"MERGE (keyword:Keyword {name: trim(toLower(line.Keyword))})" + "MERGE (keyword)-[:KEYWORDS]->(paper);");
		} catch (Exception e){
			e.printStackTrace();
		}
		
		//load cited paper/author csv
		try (Session session = driver.session()){
			session.run("USING PERIODIC COMMIT " + "LOAD CSV WITH HEADERS FROM 'file:/inputCiteAuthPaper.csv' AS line " + 
		"MERGE (cites:Cites {name: trim(toLower(line.Cites))})" + 
					"MERGE (author:Author {name: trim(toLower(line.Author))})" + "MERGE (author)-[:WROTE]->(cites);");
		} catch (Exception e){
			e.printStackTrace();
		}
		
		//match where paper with papers it cites, create relationship
		try (Session session = driver.session()){
			session.run("MATCH (p:Paper) \r\n" + 
					"MATCH (c:Cites) WHERE p.name = c.name \r\n" + "MERGE (p)-[:CITED_BY]->(c)\r\n" 
					);
		} catch (Exception e){
			e.printStackTrace();
		}
		
		//where nodes exist with the same name property, merge them together
		try (Session session = driver.session()){
			session.run("MATCH (p:Paper), (c:Cites)\r\n" + 
					"WHERE toLower(p.name) = toLower(c.name)\r\n" + 
					"call apoc.refactor.mergeNodes([p,c]) YIELD node\r\n" + "RETURN node\r\n"
					);
		} catch (Exception e){
			e.printStackTrace();
		}
		
		//where nodes with same name have been merged, remove the self-reference loop
		try (Session session = driver.session()){
			session.run("MATCH (p:Paper) -[r]->(c:Cites)\r\n" + "WHERE p.name=c.name\r\n" + 
					"DELETE r\r\n"
					);
		} catch (Exception e){
			e.printStackTrace();
		}
		
		//where nodes with same name have been merged, remove the "Cites" label to allow further uploaded files to match with
		//previously uploaded cited papers
		try (Session session = driver.session()){
			session.run("MATCH (p:Paper), (c:Cites)\r\n" + "WHERE p.name=c.name\r\n" + 
					"REMOVE c:Cites RETURN p\r\n"
					);
		} catch (Exception e){
			e.printStackTrace();
		}
		
		//remove any duplicate relationships
		try (Session session = driver.session()){
			session.run("MATCH (a)-[r]->(b)\r\n" + "WITH a, b, TAIL (COLLECT (r)) as rr\r\n" + 
					"FOREACH (r IN rr | DELETE r)\r\n"
					);
		} catch (Exception e){
			e.printStackTrace();
		}
				
		driver.close();
		
		
		try {
			file.close();
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}//end main

}//end class
