import os
import time
import requests
import pandas as pd
import json
from io import StringIO
from sqlalchemy import create_engine


def main():
    # Configuration data
    spreadsheet_csv_url = 'https://docs.google.com/spreadsheets/d/e/2PACX-1vR51bmzKMTvCfa1UKf454nnlNBCUVMtVNQvxdAiYU09E5pWS7mbsrVt45ABsCGZTByt9N_YEgnSwj8V/pub?gid=0&single=true&output=csv'
    conversion_csv_url = 'https://docs.google.com/spreadsheets/d/e/2PACX-1vRmvHzUc6_UUKbcvRche7AVebNoljqC1bf3iccusJqW9-C3k0KtESJxOCXShykSejIarAB2jHJ2bHCb/pub?gid=0&single=true&output=csv'
    pokemon_data_dir = '../common/src/main/resources/data/cobblemon/species'
    sqlite_db_name = 'pokemon_drops_data.sqlite'
    sqlite_table_name = 'pokemon_drops'

    # Download the CSV from the Google Spreadsheet
    csv_data = download_spreadsheet_data(spreadsheet_csv_url)
    csv_data_for_matching = download_spreadsheet_data(conversion_csv_url)

    # Load the data into a DataFrame
    df = load_data_from_csv(csv_data)
    mapping_df = load_data_from_csv(csv_data_for_matching)

    # Create a mapping dictionary from the Pokémon names to the Minecraft IDs
    mapping_dict = dict(zip(mapping_df['natural_name'], mapping_df['minecraft_ID']))

    # Replace the Item names with the Minecraft IDs
    df['Drops'] = df['Drops'].apply(lambda x: replace_names_in_string(x, mapping_dict))

    # Filter filenames from ..\common\src\main\resources\data\cobblemon\species based on Pokémon names that have drops
    filesToChange = filter_filenames_by_pokemon_names(pokemon_data_dir, df['Pokémon'])

    # For each file, replace the drops or create them if not present
    for file in filesToChange:
        with open(pokemon_data_dir + "/" + file, 'r', encoding="utf8") as f:
            data = json.load(f)  # Deserialize JSON to Python object

        for index, row in df.iterrows():
            if row['Pokémon'].lower() == file.split('/')[-1][:-5].lower():
                if row['Drops'] != row['Drops'] or row['Drops'] == '':
                    # Remove drops field
                    data.pop('drops', None)
                    break
                # Instead of string manipulation, just modify the Python object directly
                if "REMOVED" not in row['Drops']:
                    data['drops'] = parse_drops(row['Drops'])
                break

        with open(pokemon_data_dir + "/" + file, 'w', encoding="utf8") as f:
            json.dump(data, f, ensure_ascii=False, indent=2)  # Serialize Python object to JSON

    # Save data to SQLite
    write_to_sqlite(df, sqlite_db_name, sqlite_table_name)


def parse_drops(drops_str):
    entries = []
    drops_parts = drops_str.split(', ')
    amount = 0 # Calculate the max amount of drops possible (if everything rolls max)
    noOr = True
    if "OR" in drops_str:
        noOr = False
        drops_parts = drops_str.split(" OR ")
        amount = 1

    for part in drops_parts:
        item_info = part.split(' ')
        item_id = item_info[0]

        if "minecraft:" not in item_id and "cobblemon:" not in item_id:
            print("Item ID: " + item_id)

        currentDrop = {"item": item_id}

        # Iterate over remaining item info fields and add their values to the currentDrop
        for i in range(1, len(item_info)):
            if "%" in item_info[i]:
                percentage = float(item_info[i].replace('%', ''))
                currentDrop.update({"percentage": percentage})
                if noOr:
                    amount += 1
            elif '-' in item_info[i]:
                if noOr:
                    amount += (int(item_info[i].split('-')[1]))
                quantityRange = item_info[i]
                currentDrop.update({"quantityRange": quantityRange})
            elif '(Nether)' in item_info[i] or '(End)' in item_info[i] or '(Overworld)' in item_info[i] or item_info[i] == '':
                # Do nothing
                pass
            else:
                quantity = item_info[i]
                if quantity != "1":
                    currentDrop.update({"quantityRange": quantity})
                if noOr:
                    amount += (int(item_info[i]))

        if len(item_info) == 1:
            if noOr:
                amount += 1

        entries.append(currentDrop)

    return {
        "amount": amount,
        "entries": entries
    }


def replace_names_in_string(drop_str, mapping_dict):
    for natural_name, minecraft_id in mapping_dict.items():
        if drop_str != drop_str:  # NaN check
            break
        drop_str = drop_str.replace(natural_name, minecraft_id)
    return drop_str


def load_and_filter_data(filename, columns, filter_column, filter_value):
    df = pd.read_csv(filename, sep=',', usecols=columns)
    df = df[df[filter_column] == filter_value]
    return df


def download_spreadsheet_data(url, max_retries=5):
    delay = 1
    for attempt in range(max_retries):
        try:
            response = requests.get(url)
            response.raise_for_status()  # will raise an HTTPError if the HTTP request returned an unsuccessful status code
            return response.content.decode('utf-8')
        except requests.RequestException as e:
            if attempt < max_retries - 1:
                time.sleep(delay)
                delay *= 2
            else:
                raise e


def load_data_from_csv(csv_data):
    print("CSV data:")
    print(csv_data)
    print("----------- END OF CSV DATA -----------")
    return pd.read_csv(StringIO(csv_data), encoding='utf8', engine='python', dtype={'Pokémon': str, 'Drops': str})


def filter_filenames_by_pokemon_names(directory, pokemon_names):
    # Get list of subdirectories in the provided directory
    subdirectories = [d for d in os.listdir(directory) if os.path.isdir(os.path.join(directory, d))]

    all_files = []
    for subdir in subdirectories:
        # List all files from the subdirectory
        files_in_subdir = os.listdir(os.path.join(directory, subdir))
        # Extend the all_files list with these files
        # While adding, prepend the subdirectory name
        all_files.extend([f"{subdir}/{file}" for file in files_in_subdir])

    filtered_files = [file for file in all_files if
                      file.split('/')[-1][:-5].lower() in pokemon_names.str.lower().tolist()]
    return filtered_files


def write_to_sqlite(df, db_name, table_name):
    engine = create_engine(f'sqlite:///{db_name}', echo=True)
    df.to_sql(table_name, con=engine, if_exists='replace', index=False)


if __name__ == "__main__":
    main()
