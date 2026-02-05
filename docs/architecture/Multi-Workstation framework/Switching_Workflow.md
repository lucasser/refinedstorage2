```mermaid
graph TD;
    1(Press button)
    2(Save current workstation to data)
    3(Deposit items)
        3A(Storage Full?)
        3B(Inv Full?)
    4(Unload from memory and remove screen)
    5(Parse new workstation)
    6()
    7(Pull items)
    
    1-->2;
    2-->3;
    3-->4;
    3-->3A
    
```