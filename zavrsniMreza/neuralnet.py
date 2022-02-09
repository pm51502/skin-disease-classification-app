import numpy as np
from tensorflow import keras
from PIL import Image
import sys

diseases_dict = {
    0: 'Melanocytic nevi',
    1: 'Melanoma',
    2: 'Benign keratosis-like lesions ',
    3: 'Basal cell carcinoma',
    4: 'Actinic keratoses',
    5: 'Vascular lesions',
    6: 'Dermatofibroma'
}

if __name__ == "__main__":

    pathToModel = "F:/zavrsniMreza/model.h5"
    pathToImage = "F:/zavrsniMreza/images/skin2.jpg"

    #pathToModel = "model.h5"
    model = keras.models.load_model(pathToModel)

    model.build()
    #model.summary()

    #pathToImage = "images/skin2.jpg"
    image = np.asarray(Image.open(pathToImage).resize((100,75)))
    image = image.reshape(1, *(75, 100, 3))

    classified = model.predict(image)
    disease_index = np.argmax(classified, axis=1)[0]

    disease = diseases_dict.get(disease_index)
    print(disease)



