package uni.projecte.ui;


import uni.projecte.R;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

public class CustomEditableCell extends LinearLayout {
	
	View view;
	EditText et;
	long citationId;
	long fieldId;

	public CustomEditableCell(Context context) {
		super(context);
		 
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view=layoutInflater.inflate(R.layout.customedcell,this);
		
		et= ((EditText) view.findViewById(R.id.tvCustomCell));
		et.setImeOptions(EditorInfo.IME_ACTION_DONE);

		
		et.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged (Editable s){

			}
			public void beforeTextChanged (CharSequence s, int start, int count, int after){
	

			}
			public void onTextChanged (CharSequence s, int start,int before,int count) {

			}

		
	}
		
	); 
	
	}
	
	public void setText(String text,long citationId, long fieldId){
		
		et.setText(text);
		this.citationId=citationId;
		this.fieldId=fieldId;
		
		
	}
	
	public String getText(){
		
		return et.getText().toString();
		
	}

	public long getCitationId() {
		return citationId;
	}

	public long getFieldId() {
		return fieldId;
	}
	
	
	
}
