import bcrypt
from pymongo import MongoClient

mongo_uri = "mongodb://192.168.1.62:27017/"
database_name = "SudolDB"
users_collection_name = "Users"
roles_collection_name = "Roles"

client = MongoClient(mongo_uri)
database = client[database_name]

def hash_password(password):
    hashed_password = bcrypt.hashpw(password.encode('utf-8'), bcrypt.gensalt(rounds=10))
    return hashed_password.decode('utf-8')

def add_user():
    username = input("Wprowadź nazwę użytkownika: ")
    password = input("Wprowadź hasło: ")
    role = input("Wprowadź rolę (ROLE_ADMIN lub ROLE_TEACHER): ")

    if database[users_collection_name].find_one({"username": username}):
        print("Użytkownik o takiej nazwie już istnieje!")
        return

    hashed_password = hash_password(password)

    role_query = {"name": role}
    role_data = database[roles_collection_name].find_one(role_query)

    if not role_data:
        print("Nie znaleziono roli.")
        return

    new_user = {
        "username": username,
        "password": hashed_password,
        "roles": [{"$ref": roles_collection_name, "$id": role_data["_id"]}],
    }

    database[users_collection_name].insert_one(new_user)
    print("Użytkownik zarejestrowany poprawnie!")

def display_all_users():
    users = database[users_collection_name].find({}, {"_id": 0, "password": 0})
    for user in users:
        print(user)

def display_all_roles():
    roles = database[roles_collection_name].find({}, {"_id": 0})
    for role in roles:
        print(role)

def delete_user_by_name():
    username = input("Wprowadź nazwę użytkownika do usunięcia: ")
    result = database[users_collection_name].delete_one({"username": username})
    if result.deleted_count > 0:
        print(f"Użytkownik '{username}' usunięty pomyślnie.")
    else:
        print(f"Nie znaleziono użytkownika o nazwie '{username}'.")

def update_user_password():
    username = input("Wprowadź nazwę użytkownika do aktualizacji hasła: ")
    new_password = input("Wprowadź nowe hasło: ")

    hashed_password = hash_password(new_password)

    result = database[users_collection_name].update_one(
        {"username": username},
        {"$set": {"password": hashed_password}}
    )

    if result.modified_count > 0:
        print(f"Hasło użytkownika '{username}' zaktualizowane pomyślnie.")
    else:
        print(f"Nie znaleziono użytkownika o nazwie '{username}'.")

def update_user_role():
    username = input("Wprowadź nazwę użytkownika do aktualizacji roli: ")
    new_role = input("Wprowadź nową rolę (ROLE_ADMIN lub ROLE_TEACHER): ")

    role_query = {"name": new_role}
    role_data = database[roles_collection_name].find_one(role_query)

    if not role_data:
        print("Nie znaleziono nowej roli.")
        return

    result = database[users_collection_name].update_one(
        {"username": username},
        {"$set": {"roles": [{"$ref": roles_collection_name, "$id": role_data["_id"]}]}}
    )

    if result.modified_count > 0:
        print(f"Rola użytkownika '{username}' zaktualizowana pomyślnie.")
    else:
        print(f"Nie znaleziono użytkownika o nazwie '{username}'.")

def main():
    while True:
        print("\n1. Dodaj użytkownika")
        print("2. Wyświetl użytkowników")
        print("3. Wyświetl role")
        print("4. Usuń użytkownika")
        print("5. Zaktualizuj hasło użytkownika")
        print("6. Zaktualizuj rolę użytkownika")
        print("7. Wyjdź")

        choice = input("Wybór (1-7): ")

        if choice == "1":
            add_user()
        elif choice == "2":
            display_all_users()
        elif choice == "3":
            display_all_roles()
        elif choice == "4":
            delete_user_by_name()
        elif choice == "5":
            update_user_password()
        elif choice == "6":
            update_user_role()
        elif choice == "7":
            break
        else:
            print("Nieprawidłowa opcja, spróbuj ponownie (1-7).")

if __name__ == "__main__":
    main()
