package uni.projecte.dataLayer.ProjectManager.examples;

import android.content.Context;
import uni.projecte.controler.ProjectControler;

public class ExampleProjectCreator {
	
	private ProjectControler projCnt;
	private Context context;
	private String language;
	
	

	public ExampleProjectCreator(Context c, String language){
		
		this.context=c;
		this.language=language;
		projCnt=new ProjectControler(context);
		
	}
	
	public String getProjectName() {
		
		if(language.equals("ca")){
			
			return "Projecte de prova";
			
			
		}
		else if(language.equals("es")){
			
			return "Proyecto de prueba";

			
		}
		else if(language.equals("en")){
			
			return "Demo project";

			
		}
		else{
			
			return "Test project";
			
			
		}

		
	}

	
	public long createBasicProject(String projectName){
		
		long projId=-1;
		
		if(language.equals("ca")){
			
			projId=projCnt.createProject(projectName, "","");
			
			if(projId>0){
				
				projCnt.startTransaction();
					projCnt.addProjectField(projId, "CitationNotes", "Comentari", "", "comentari per defecte", "simple", "ADDED");
					projCnt.addProjectField(projId, "locality", "Localitat", "", "", "simple", "ADDED");
					projCnt.addProjectField(projId, "photo", "Fotografia", "", "", "photo", "ADDED");
				projCnt.endTransaction();
			
			}

			
		}
		else if(language.equals("es")){
			
			projId=projCnt.createProject(projectName, "","");
			
			if(projId>0){
				
				projCnt.startTransaction();
					projCnt.addProjectField(projId, "CitationNotes", "Comentario", "", "comentario por defecto", "simple", "ADDED");
					projCnt.addProjectField(projId, "locality", "Localidad", "", "", "simple", "ADDED");
					projCnt.addProjectField(projId, "photo", "Fotografía", "", "", "photo", "ADDED");
				projCnt.endTransaction();
			
			}

			
			
		}
		else if(language.equals("en")){
			
			projId=projCnt.createProject(projectName, "","");
			
			if(projId>0){
				
				projCnt.startTransaction();
					projCnt.addProjectField(projId, "CitationNotes", "Comment", "", "comentari per defecte", "simple", "ADDED");
					projCnt.addProjectField(projId, "locality", "Locality", "", "", "simple", "ADDED");
					projCnt.addProjectField(projId, "photo", "Photo", "", "", "photo", "ADDED");
				projCnt.endTransaction();
			
			}

			
			
		}
		else{
			
		projId=projCnt.createProject(projectName, "","");
			
			if(projId>0){
				
				projCnt.startTransaction();
					projCnt.addProjectField(projId, "CitationNotes", "Commentaire", "", "comentari per defecte", "simple", "ADDED");
					projCnt.addProjectField(projId, "locality", "Lieu", "", "", "simple", "ADDED");
					projCnt.addProjectField(projId, "photo", "Photographie", "", "", "photo", "ADDED");
				projCnt.endTransaction();
			
			}

			
			
		}
		
		return projId;
		
	}
	

}
