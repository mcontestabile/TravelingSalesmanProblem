# TravelingSalesmanProblem

#eng  
Find the optimum solution for the graph G, return the ObjVal and the optimum path.  
Find another optimum path and print the new path.  
Find another path and ObjVal with those constraints:  
a. the cost for the incident sides in v must be a% of the total cost;  
b. if (b1,b2) side is run across, the optimum cost must be less than c;  
c. side (d1,d2) must be run across only if are run across (e1,e2) adn (f1,f2);  
d. if (g1,g2), (h1,h2) and (i1,i2) sides are run across, there's a cost penality by l.  

#it  
I Trovare la soluzione ottima per il Problema del Commesso Viaggiatore su G, riportando il valore della funzione obiettivo e il ciclo ottimo individuato.  
II Implementare una metodologia per verificare la presenza di un ulteriore ciclo ottimo di costo equivalente a quello riportato al punto I. Riportare quindi il ciclo trovato.  
III Apportare le opportune modifiche al modello utilizzato al punto I per includere le seguenti restrizioni:  
a. il costo dei lati incidenti al vertice v sia al massimo il a% del costo totale del ciclo;  
b. se il lato (b1,b2) viene percorso, il costo del ciclo ottimo sia inferiore a c;  
c. il lato (d1,d2) sia percorribile se e solo se sono percorsi anche i lati (e1,e2) e (f1,f2);  
d. nel caso in cui i lati (g1,g2), (h1,h2) e (i1,i2) vengano tutti percorsi, si debba pagare un costo aggiuntivo
pari a l.  
Riportare il valore della funzione obiettivo e il ciclo ottimo di questo nuovo modello.  

