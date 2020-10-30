from pathlib import Path
import os

filelocation = '../../../Data Set/csv/2791_2019dar.csv'


class Main:
    def __init__(self):
        # Opens the file using fileio.
        # The Path() allows it to work on both mac and windows. __file__ returns the file location.
        with open(Path(os.path.dirname(__file__) + filelocation), 'r') as f:

            results = {}  # This will store all our information organized
            matchdatalist = []  # This is a list full of the keys used in results

            for index, line in enumerate(f, start=1):

                data = line.split(",")  # Separate data by comma
                matchdata = {}  # Stores the data of the match

                if index != 1:  # If not the matchdata list
                    teamnumber = data[0]
                    matchnum = data[1]

                    for index, value in enumerate(data):
                        if index > 1:  # Filter out the team number
                            matchdata.update({str(matchdatalist[index]): value})

                    if not (str(teamnumber) in results):  # First match
                        updatedata = {}
                        updatedata.update({teamnumber: {}})  # Puts teamnumber into the results
                        updatedata[teamnumber].update({matchnum: {}})  # Puts match number into the team
                        updatedata[teamnumber][matchnum].update(
                            matchdata.items())  # puts the match data into the match number
                        results.update(updatedata)  # Push data to results
                    else:  # 2+ matches
                        updatedata = {}
                        updatedata.update({matchnum: {}})  # Puts match number into the team
                        updatedata[matchnum].update(matchdata.items())  # puts the match data into the match number
                        results[teamnumber].update(updatedata)  # Push data to results
                else:
                    data = line.split(",")  # Separate data by comma
                    for key in data:
                        matchdatalist.append(key)  # Push keys to matchdatalist

            for match in results["51"]:
                print(match, end=": ")
                print(results["51"][match])


if __name__ == '__main__':
    Main()
