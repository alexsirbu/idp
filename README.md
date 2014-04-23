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

Tema 2 IDP:
===========

Tema a constat in realizarea modulului de networking si de interfatarea acestuia
cu GUI-ul aplicatiei prin intermediul unui mediator.

Modulul de networking este un modul standalone, decuplat complet de restul
aplicatiei, expunand o serie de functii specifice precum connect, read si write dar
si o functie de registerObserver care permite inregistrarea observerilor cu privire
la event-urile aparute in cadrul modulului de networking. Modulul implementeaza atat
functii specifice client dar si server. Serverul este implementat multithreaded,
utilizand un thread principal care lanseaza in executie threaduri de prelucrare a
operatiilor de networking(read, write, connect, accept). In cazul operatiilor
connect si accept care presupun executarea unei singuri instructiuni nonblocante,
executia acestora nu este lansata in cadrul altui thread, ci a celui principal.
Pentru limitarea numarului de threaduri si a modela comportamentul de server
multithreaded, este utlizat un ExecutionPool. In momentul in care se accepta sau
se realizeaza o noua conexiune, nu este setata nici o operatie de interes pe 
aceasta, responsabilitatea fiind pasata observerilor care utilizeaza modulul(acesta
expune doar o serie de primitive). In cazul in care se executa read este setata
operatia de READ ca fiind de interes pe selection key, iar in cazul in care se
execut write operatia de WRITE este setata ca fiind de interes. Functiile expuse
pentru utilizarea de observeri sunt asincrone, acestea doar adaugand continutul
de scris in buffer in cazul lui write si setand operatia de interes. In momentul
in care threadul principal ajunge la executia operatiilor de read/write reseteaza
operatia de interes la 0 pentru a evita executia in paralel a mai multor threaduri
de operatii pe acelasi socket channel, iar responsabilitatea setarii acetuia
este lasata threadurilor/observerilor. Fiecare operatie are asociat si un timer.
Threadul operatiei read citeste cat timp numarul de octeti cititi este nenul,
seteaza flagurile de error encountered/timeout/end of stream, realizeaza o
parsare initiala a continutului si notifica observerii in cazul aparitie unui
eveniment. In cazul in care nu a aparut un eveniment de tip error/timeout/end of
stream iar octetii cititi pana la momentul respectiv nu reprezinta un mesaj intreg,
threadul seteaza din nou operatia de READ ca fiind de interes. La fiecare executie
a unui thread READ daca se reuseste citirea a cel putin un octet este resetat timerul.
Threadul operatiei write incearca sa scrie in timeoutul furnizat toti octetii furnizati
si notifica la final observerii cu privire la starea operatiei. Pentru mentinerea starii
unei conexiuni sunt utilizate clasele de stari operatii. Intre modulul de networking
si mediator este intercalat un adaptor care translateaza callurile din GUI in networking si
invers. Acesta se inregistreaza ca observer la modulul de networking si contine logica
protocolului implementat.

Protocolul pe care l-am utilizat in cadrul aplicatiei este urmatorul:
- mesajele care se trimit intre entitati sunt NetworkMessages, care inglobeaza un mesaj 
efectiv si contine la inceput lungimea efectiva a mesajului, pentru a sti care este 
lungimea asteptata
- protocolul in sine consta in 4 tipuri de mesaje:
-- MessageRequestFile - cerere de fisier, contine numele fisierului si numele clientului
care il cere, pentru a-l putea afisa cel care raspunde in interfata
-- MessageRequestFileResponse - raspuns la mesajul anterior, ce contine filesize-ul 
-- MessageRequestFilePart - cerere de content, de o anumita lungime de la o pozitie; 
se cer bucati de maxim 500 de octeti, pana se termina fisierul
-- MessageRequestFilePartResponse - raspuns la mesajul anterior, contine content-ul cerut
In realizarea acestui protocol am folosit Factory pattern, pentru decodarea mesajelor
venite pe retea.

Am realizat o interfata pentru operatii cu fisiere si o implementare, cu Java.NIO, pentru
operatii uzuale de citire si scriere.

Am realizt si testarea cu ajutorul JUnit, pentru operatiile cu fisiere si pentru operatiile
cu mesaje (encodare, decodare, folosire factory, creare mesaje networking). Am considerat
ca pentru aceste clase se preteaza testarea unitara, deoarece partea de networking, in sine, 
era greu de testat cu astfel de teste (pentru GUI am realizat teste etapa anterioara).

Logarea cu log4j am facut-o cum s-a specificat in enunt, cu fisier de configurare, in fisiere
pentru fiecare client si la consola, diferentiat in functie de nivelul de logging.

Pentru configurare, am folosit indicatiile din enunt, folosind aceleasi fisiere, singura diferenta
constand in structura fisierului de config din folderul specific - acesta contine si hostname-ul
si portul, pe langa fisiere (nu exista o alta modalitate prin care sa se primeasca aceste informatii).
Implementarea actuala poate sa fie usor modificata pentru tema 3.

De asemenea, am realizat si build.xml-ul care instantiaza 3 clienti, client1, client2, client3, 
atasand si fisierele corespunzatoare pentru a putea testa. Acestia pornesc in ordine, cu timpi de
asteptare inca. Fisierul corespunzator clientului 2 este multipart conform protocolului nostru,
pentru a vedea trimiterea in mai multe parti.

Distributia taskurilor a fost urmatoarea:
- Cristi - modulul de networking 
- Alex - mesaje, logging, build.xml, operatii cu fisiere, teste
- amandoi - adaptorul
Consideram ca, impartind astfel taskurile, workload-ul a fost asemanator, si fiecare a putut sa 
lucreze la partea lui fara sa-l influenteze major pe celalalt, pana la partea de integrare.
