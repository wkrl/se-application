/**
 * Project SE-Application
 */


#ifndef _CUSTOMER_H
#define _CUSTOMER_H

class Customer {
public: 
    
/**
 * @param name
 */
Customer Customer(String name);
    
String getId();
    
String getName();
    
/**
 * @param name
 */
Customer setName(String name);
private: 
    String id;
    String name;
    List<Note> notes;
};

#endif //_CUSTOMER_H