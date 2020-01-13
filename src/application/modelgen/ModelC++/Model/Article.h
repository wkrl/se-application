/**
 * Project SE-Application
 */


#ifndef _ARTICLE_H
#define _ARTICLE_H

class Article {
public: 
    
/**
 * @param name
 * @param price
 */
Article Article(String name, String price);
    
String getId();
    
String getName();
    
String getPrice();
    
/**
 * @param name
 */
Article setName(String name);
    
/**
 * @param price
 */
Article setPrice(String price);
private: 
    String id;
    String name;
    String price;
    
/**
 * @param id
 * @param name
 * @param price
 */
void Article(String id, String name, String price);
};

#endif //_ARTICLE_H