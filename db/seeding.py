from datetime import datetime, timedelta
import random

start_date = datetime(1981, 7, 12)
end_date = datetime.today()

values = []
current_date = start_date

while current_date <= end_date:
    date_str = current_date.strftime('%Y-%m-%d')
    value = round(random.uniform(-100, 100), 2)
    values.append(f"('{date_str}', {value})")
    current_date += timedelta(days=1)

print("INSERT INTO calculations\n\t(date, number)\nVALUES")
print(",\n".join(f"\t{v}" for v in values) + ";")
