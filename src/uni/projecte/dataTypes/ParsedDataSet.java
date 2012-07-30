/*    	This file is part of ZamiaDroid.
*
*	ZamiaDroid is free software: you can redistribute it and/or modify
*	it under the terms of the GNU General Public License as published by
*	the Free Software Foundation, either version 3 of the License, or
*	(at your option) any later version.
*
*    	ZamiaDroid is distributed in the hope that it will be useful,
*    	but WITHOUT ANY WARRANTY; without even the implied warranty of
*    	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    	GNU General Public License for more details.
*
*    	You should have received a copy of the GNU General Public License
*    	along with ZamiaDroid.  If not, see <http://www.gnu.org/licenses/>.
*/

package uni.projecte.dataTypes;


public class ParsedDataSet {
     private String extractedString = null;
     private int extractedInt = 0;

     public String getExtractedString() {
          return extractedString;
     }
     public void setExtractedString(String extractedString) {
          this.extractedString = extractedString;
     }

     public int getExtractedInt() {
          return extractedInt;
     }
     public void setExtractedInt(int extractedInt) {
          this.extractedInt = extractedInt;
     }
     
     @Override
	public String toString(){
          return "ExtractedString = " + this.extractedString
                    + "\nExtractedInt = " + this.extractedInt;
     }
}