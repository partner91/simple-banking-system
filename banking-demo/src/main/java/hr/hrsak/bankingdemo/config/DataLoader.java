package hr.hrsak.bankingdemo.config;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import hr.hrsak.bankingdemo.dto.TransactionDTO;
import hr.hrsak.bankingdemo.mappers.TransactionMapper;
import hr.hrsak.bankingdemo.models.CurrencyEnum;
import hr.hrsak.bankingdemo.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Configuration
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final ResourceLoader resourceLoader;
    private final int BATCH_SIZE = 1000;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public DataLoader(ResourceLoader resourceLoader, TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.resourceLoader = resourceLoader;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public void run(String... args) {
        Resource resource = resourceLoader.getResource("classpath:data/transactions/transactions.csv");
        try(CSVReader csvReader = new CSVReader(new FileReader(resource.getFile()));
            ExecutorService executorService = Executors.newFixedThreadPool(10)) {
            List<String[]> transactions = csvReader.readAll();
            int dataSize = transactions.size();
            for (int i = 0; i < dataSize; i+=BATCH_SIZE) {
                final int start = i;
                final int end = Math.min(dataSize, i + BATCH_SIZE);
                executorService.submit(() -> {
                   createNewTransaction(transactions.subList(start, end));
                });
            }
            executorService.shutdown();
        } catch (IOException | CsvException ex) {
           log.error(ex.getMessage());
        }

    }

    private void createNewTransaction(List<String[]> transactions) {
        List<TransactionDTO> transactionDTOs = new ArrayList<>();
        transactions.forEach(transaction -> {
            transactionDTOs.add(
                    convertStringArrayToTransactionDTO(transaction)
            );
        });
        transactionRepository.saveAll(transactionDTOs.stream().map(transactionMapper::fromDTO).toList());

    }

    private TransactionDTO convertStringArrayToTransactionDTO(String[] transaction) {
        return  TransactionDTO.builder()
                .senderAccountId(Integer.parseInt(transaction[0]))
                .receiverAccountId(Integer.parseInt(transaction[1]))
                .amount(new BigDecimal(transaction[2]))
                .currencyId(CurrencyEnum.valueOf(transaction[3]))
                .message(transaction[4])
                .timeStamp(Timestamp.valueOf(LocalDateTime.parse(transaction[5])))
                .build();
    }

}
