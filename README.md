### Voraussetzungen

- Python und der Node package manager sind auf dem System installiert

- der localhost auf port 8000 wird noch nicht verwendet
### Vor dem ersten Start

- in ./maegges/backend/ ein Python virtual environment erstellen mit ```python -m venv```. (Wenn python commands nicht funktionieren, statt ```python``` ```python3``` verwenden)

- das erstellte virtual environment als source setzen mit ```source ./.venv/bin/activate```

- die python requirements installieren mit ```pip install -r requirements.txt```

- in ./maegges/frontend/ wechseln und die node dependencies mit ```npm install``` installieren

### Starten

- in ./maegges/backend/ main.py via ```python main.py``` starten

- die Applikation im Browser auf dem localhost:8000 öffnen

- Es gibt zwei Accounts die wir schon im Voraus angelegt haben:
	- name: Alice | passwort: 1234
	- name: Bob  | passwort: 1234

- Man kann auch über den Knopf "Create Account" einen neuen User mit den Eingaben in den Textfeldern erstellen.

- **Achtung! Das Senden von Name und Passwort wird in Plain-Text durchgeführt und so auch in der Datenbank nach dem schließen der Anwendung im Backend gespeichert!**

- Um die Anwendung zu schließen ins Terminal wechseln, in dem main.py gestartet wurde und mit ```^C``` beenden


### Unsere Anwendung

