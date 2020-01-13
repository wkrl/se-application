/**
 * Project SE-Application
 */


#ifndef _NOTE_H
#define _NOTE_H

class Note {
public: 
    
/**
 * @param noteStr
 */
Note Note(String noteStr);
    
String getText();
    
/**
 * @param text
 */
void setText(String text);
private: 
    Date timeStamp;
    String noteText;
    
/**
 * @param noteStr
 */
vector<Object> parselogStr(String noteStr);
    
Date nextUniqueTimeStamp();
};

#endif //_NOTE_H