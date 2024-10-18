import nltk
from nltk.chat.util import Chat, reflections
from nltk.sentiment.vader import SentimentIntensityAnalyzer
from spellchecker import SpellChecker

# Download NLTK data (if not done already)
nltk.download('punkt')
nltk.download('vader_lexicon')

# Initialize sentiment analyzer and spellchecker
sid = SentimentIntensityAnalyzer()
spell = SpellChecker()

# Define pairs of patterns, responses, and follow-ups
# Using keywords, tense variations, and more detailed responses
patterns_responses = {
    "feeling stressed": "It sounds like you're feeling overwhelmed. Would you like to try some relaxation techniques?",
    "feeling anxious": "Anxiety can be hard to manage. Do you want to talk about what’s causing this?",
    "can't sleep": "Sleep troubles can be difficult. Have you tried relaxation exercises before bed?",
    "feel alone": "It’s okay to feel lonely sometimes. Do you have anyone you trust to talk to about this?",
    "need help": "Seeking help is an important step. I can assist you in finding resources or a mental health professional.",
    "feel hopeless": "I'm sorry to hear that. Please remember that things can get better. Would you like some resources for support?",
    "breathing exercise": "Sure, here's one: Breathe in slowly for 4 seconds, hold your breath for 4 seconds, and exhale slowly for 4 seconds. Try this for a few minutes.",
    "exercise help": "Exercise can be a great way to reduce stress. Do you need help finding some exercises that might help?",
    "talk to someone": "Talking to someone can help. Would you like assistance in finding a counselor or therapist?",
    "feel down": "It’s okay to feel low sometimes. Do you want to share more about what’s going on?",
    "sad": "I'm really sorry you're feeling sad. Is there anything specific that's making you feel this way?",
    "frustrated": "Frustration can be tough. Would you like some techniques for calming down?",
    "feel better": "I’m glad to hear that! If there’s anything else I can assist you with, feel free to ask.",
}

# Function to correct typos in user input
def correct_typos(user_input):
    words = user_input.split()
    corrected_words = [spell.correction(word) for word in words]
    corrected_input = " ".join(corrected_words)
    return corrected_input

# Function to get sentiment of the user input
def get_sentiment(user_input):
    sentiment_score = sid.polarity_scores(user_input)
    if sentiment_score['compound'] >= 0.05:
        return 'positive'
    elif sentiment_score['compound'] <= -0.05:
        return 'negative'
    else:
        return 'neutral'

# Function to match keywords in user input with responses
def get_response(user_input):
    user_input = user_input.lower()  # Case-insensitive matching
    for pattern, response in patterns_responses.items():
        if pattern in user_input:
            return response
    return "I'm here to listen. Can you tell me more about how you're feeling?"

# Start conversation with sentiment analysis and typo correction
print("Hi, I'm here to help. You can talk to me about how you're feeling.")
while True:
    user_input = input("You: ")
    
    if user_input.lower() in ['quit', 'exit']:
        print("Goodbye! Take care!")
        break

    # Correct any typos in user input
    corrected_input = correct_typos(user_input)
    
    # Analyze sentiment of corrected input
    sentiment = get_sentiment(corrected_input)
    
    # Get response based on keyword detection and sentiment
    if sentiment == 'positive':
        response = "I'm glad you're feeling good! How can I assist you further?"
    elif sentiment == 'negative':
        response = "I'm sorry to hear that you're feeling this way. How can I support you?"
    else:
        response = get_response(corrected_input)

    print(f"Bot: {response}")
