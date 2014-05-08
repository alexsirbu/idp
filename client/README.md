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

Tema 3 IDP:
===========

Tema a constat in implementarea serviciului web pentru aplicatia de sharing fisiere Sharix si in
integrarea acestuia cu aplicatia. Serviciul a fost implementat utilizand Tomcat 6.0 si Axis2 1.2 si
utilizand facilitatea de generare serviciu web si client pentru acesta a Eclipse porning de la o
clasa care sa contina functionalitatea serviciului. Dificultatea acestei portiuni a temei a fost
reprezentata de instalarea tuturor componentelor necesare in eclipse si configurarea acestora.
Stubul de server pentru client are un comportament sincron pentru simplificarea integrarii acestuia.
Serviciul web expune urmatoarele metode:
	- registerPeer - primeste ca parametrii numele peerului, adresa IP si portul
	- unregisterPeer - primeste ca parametru numele peerului
	- getPeers - returneaza un array de stringuri ce contine numele, adresa si portul cu care
	s-au inregistrat toti clientii
	- updatePeerFileList - primeste ca parametru numele peerului si lista de fisiere a acestuia
	- getPeerFileList - primeste ca parametru numele peerului si returneaza lista de fisiere a
	acestuia.
Pentru retinerea tuturor clientilor inregistrati, serviciul utilizeaza 3 HashMapuri care asociaza
fiecarui nume de peer o adresa, un port si o lista de fisiere.

Integrarea serviciului web a constat in adaugarea unor apeluri la distanta in cadrul clientului
pentru inregistrare peer, updatare lista de fisiere precum si preluarea de la serviciu a listei
de peeri si a fisierelor acestora.

Aceste apeluri la distanta au fost (conform enuntului, nu am implementat in plus nici o functionalitate):
- la inregistrare - apel pentru inregistrare peer curent, apel adaugare fisiere peer curent, 
functie updatare peers
- functie updatare peers - eliminare peers din lista + din gui; apel de preluare nume, ip si port peers; 
pentru fiecare peer, preluare fisiere; adaugare peers din nou in lista de peers(si, implicit, si in gui)
- la inceput download, apel updatare peers
- daca se primeste un request de la un peer necunoscut, atunci isi face update la peers inainte
de a trata requestul
- cand se inchide frame-ul, apel de unregister

Updatarea listei de peers are loc doar in momentul in care se initiaza sau este initiat un download,
conform enuntului. 

Pentru executie, trebuie pornit din eclipse serviciul, apoi apeland ant se pornesc si clientii 
(ca la tema 2).

Distributia taskurilor a fost urmatoarea:
- Cristi - Serverul pt serviciul web si un server stub pentru acesta
- Alex - Integrarea serviciului web cu aplicatia, build.xml
Consideram ca, impartind astfel taskurile, workload-ul a fost asemanator, si fiecare a putut sa 
lucreze la partea lui fara sa-l influenteze major pe celalalt, pana la partea de integrare.
