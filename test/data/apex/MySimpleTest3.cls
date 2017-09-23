@isTest
public without sharing class MySimpleTest {
    public Boolean myStatus { get; set; }

    public Integer myValue = 5;

    public List<Account> myMethod(Map<String, Integer> arg1) throws Exception {
        List<Account> acctList = [SELECT Id, Name FROM Account WHERE Name = 'Taro'];
        return acctList;
    }
}
// Tail comment.