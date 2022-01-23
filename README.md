# engima-azercellhackathon
A simple summarizer app written for Azercell Hackathon 2022
Deployed on: enigma-azercellhackathon.herokuapp.com
Runs on Postgres database, connected to external heroku database.
/api/summarize executes a custom summary algorithm. It might not support some symbols, including some Azerbaijani. It also has and desktop application written with Swing.
{
  "type": "PERCENT",
  "percent": 0,
  "word": "string",
  "text": "string"
}
Types are ["PERCENT", "WORD"].
percent - should be between 0 - 100, but realistically it should be increased by 10.
word - is optional and takes a word to "focus" on. If type is "WORD" it should be included.
text - is text to summarize

/api/summarize executes a request to an external API to summarise the given text
{
  "inputFormat": "URL",
  "limitFormat": "SENTENCE",
  "sentenceLimit": 0,
  "percentLimit": 0,
  "text": "string",
  "url": "string"
}
inputFormat ["TXT", "URL"]
limitFormat ["SENTENCE", "LIMIT"]
if inputFormat is:
    TXT then "text" shouldn't be empty
    URL then "url" shouldn't be empty
if limitFormat is:
    SENTENCE then sentenceLimit shouldn't be empty
    LIMIT then percentLimit shouldn't be emtpy
  

/api/summarize/file
accepts arguments as query params. Also accepts "file" PDF file as form-data.

email: cavid.aydin3@gmail.com
phone: +994517792726
