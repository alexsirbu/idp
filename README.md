idp 
===

Nume echipa:
============
*eHonk*

Componenta echipa
==================
Pop Cristian 341C3
Sirbu Alexandru 341C3

Asistent:
=========

Catalin Gosman

Tema 1 IDP:
===========

Tema a constat in realizarea modulului grafic al aplicatiei ce se doreste a se
implementa in decursul semestrului. Pentru realizarea acestuia, noi am decis sa
realizam si un draft al modulului de mediator, pentru a ne ajuta si la testare
acum, si pe viitor la realizarea si integrarea celorlalte module.

Pentru rularea proiectului, trebuie executat doar ant in directorul principal.
Pentru simplitate, am inclus rularea mock-ului in rularea main-ului si nu necesita
o rulare suplimentara.

Pentru implementarea acestei teme, am considerat necesare clase pentru File,
in care momentan retinem numele fisierului si size-ul, Peer, in care retinem 
numele utilizatorului si lista sa de fisiere, si Transfer, in care retinem 
informatii despre un transfer (sender, receiver, fisier si status sub forma
procentului deja transferat din fisier). Aceste clase le-am folosit pentru a 
retine date despre entitatile din program si am retinut liste de Peers si 
Transferuri la nivelul mediatorului, pentru a avea separarea nivelului grafic
de nivelul de management al datelor. Astfel, am folosit modelul MVC, modelul
fiind reprezentat de aceste clase, controller-ul fiind mediatorul si view-ul fiind
modulul grafic.

In cadrul modulului grafic, am extins, in primul rand, TableCellRenderer, pentru
a face afisarea progress bar-ului in cadrul tabelului, precum si DefaultTableModel,
pentru a face celulele needitabile (cum era mentionat si in textul temei).

Pentru GUI, am creeat 3 zone, una pentru lista de peers, una pentru lista de fisiere
si una pentru tabelul cu transferuri. Am realizat listenere pentru click pe numele
peer-ului, pentru a afisa lista lui de fisiere, si listener pentru dublu click pentru
a initializa transferul acelui fisier de la userul selectat. De asemenea, am creeat
metode pentru adaugarea de useri, stergerea de useri (cu stergerea fisierelor in cazul
in care utilizatorul selectat curent este cel care este sters), updatarea fisierelor
unui utilizator, initializarea unui transfer catre un peer si updatarea progresului unui
transfer. Aceste functii sunt apelate din cadrul mediatorului, in cazul aparitiei acestor
evenimente.

Pentru testare, am creeat o clasa de test, care initializeaza lista de peers cu un numar
random de peers, fiecare cu un numar random de fisiere, si initializeaza si lista 
utilizatorului curent cu un numar de fisiere, apoi porneste un SwingWorker, care
executa, la diferite perioade de timp, una din urmatoarele actiuni:
- adaugarea unui nou peer
- stergerea unui peer existent
- modificarea listei fisierelor unui peer
- initializarea unei cereri de transfer
- updatarea unui transfer deja existent
Utilizand aceasta clasa, testam integrarea GUI-ului cu mediatorul, cum era precizat in 
enunt. Nu am realizat teste grafice (click-uri), deoarece nu era mentionat in enunt acest
lucru si am avut si probleme cu folosirea jar-ului de teste utilizat la laborator.

