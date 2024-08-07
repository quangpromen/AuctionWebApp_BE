package vn.webapp.backend.auction.service.dashboard;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.webapp.backend.auction.dto.DashBoardRequest;
import vn.webapp.backend.auction.dto.DashBoardResponse;
import vn.webapp.backend.auction.enums.AccountState;
import vn.webapp.backend.auction.enums.Role;
import vn.webapp.backend.auction.repository.*;

import java.time.Year;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class DashBoardService {
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final JewelryRepository jewelryRepository;
    private final AuctionRegistrationRepository auctionRegistrationRepository;
    private final TransactionRepository transactionRepository;

    public DashBoardResponse getInformation(DashBoardRequest dashBoardRequest) {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime startOfNextDay = startOfDay.plusDays(1);

        // Total counts
        Integer totalUser = userRepository.getTotalUser();
        Integer totalJewelryPricing = jewelryRepository.countAllJewelriesNeedPricing(dashBoardRequest.monthGetJewelry(), dashBoardRequest.yearGetJewelry());
        Integer totalJewelryPriced = jewelryRepository.countAllJewelriesPriced(dashBoardRequest.monthGetJewelry(), dashBoardRequest.yearGetJewelry());
        Integer totalJewelryNotHasAuction = jewelryRepository.countAllJewelriesNotHasAuction(dashBoardRequest.monthGetJewelry(), dashBoardRequest.yearGetJewelry());
        Integer totalJewelryHasAuction = jewelryRepository.countAllJewelriesHasAuction(dashBoardRequest.monthGetJewelry(), dashBoardRequest.yearGetJewelry());
        Integer totalJewelryHandover = jewelryRepository.countAllJewelriesHandOver(dashBoardRequest.monthGetJewelry(), dashBoardRequest.yearGetJewelry());
        Integer auctionFailed = auctionRepository.countAllAuctionsFailed(dashBoardRequest.monthGetAuctionFailedAndSuccess(), dashBoardRequest.yearGetAuctionFailedAndSuccess());
        Integer auctionSuccess = auctionRepository.countAllAuctionsSuccessful(dashBoardRequest.monthGetAuctionFailedAndSuccess(), dashBoardRequest.yearGetAuctionFailedAndSuccess());
        Integer totalUsersVerified = userRepository.getTotalUserByState(AccountState.VERIFIED);
        Integer totalUsersActive = userRepository.getTotalUserByState(AccountState.ACTIVE);
        Integer totalUsersInActive = userRepository.getTotalUserByState(AccountState.INACTIVE);
        Integer totalMembers = userRepository.getTotalUserByRole(Role.MEMBER);
        Integer totalStaffs = userRepository.getTotalUserByRole(Role.STAFF);
        Integer totalManagers = userRepository.getTotalUserByRole(Role.MANAGER);
        Integer totalAdmins = userRepository.getTotalUserByRole(Role.ADMIN);

        // Total revenues
        Double totalRevenueToday = transactionRepository.getTotalRevenueToday(startOfDay, startOfNextDay);
        Double totalRegistrationFeeRevenueToday = transactionRepository.getTotalRegistrationFeeRevenueToday(startOfDay, startOfNextDay);

        // Arrays for monthly data
        Integer[] totalUsersRegisterByMonth = new Integer[12];
        Integer[] totalAuctionByMonth = new Integer[12];
        Double[] totalRevenueByMonth = new Double[12];
        Double[] totalParticipationByMonth = new Double[12];

        // Calculate data for each month of the year
        for (int month = 1; month <= 12; month++) {
            totalUsersRegisterByMonth[month - 1] = userRepository.getTotalUserByMonthAndYear(month, dashBoardRequest.yearGetRegisterAccount());
            totalAuctionByMonth[month - 1] = auctionRepository.countAuctionsByMonthAndYear(month, dashBoardRequest.yearGetAuction());

            Double totalRevenue = transactionRepository.getTotalRevenueByMonthAndYear(month, dashBoardRequest.yearGetRevenue());
            totalRevenue = (totalRevenue != null) ? totalRevenue : 0.0;

            Double totalRegistrationFeeRevenueByMonthAndYear = transactionRepository.getTotalRegistrationFeeRevenueByMonthAndYear(month, dashBoardRequest.yearGetRevenue());
            totalRegistrationFeeRevenueByMonthAndYear = (totalRegistrationFeeRevenueByMonthAndYear != null) ? totalRegistrationFeeRevenueByMonthAndYear : 0.0;

            totalRevenueByMonth[month - 1] = totalRevenue + totalRegistrationFeeRevenueByMonthAndYear;


            Long participation = auctionRegistrationRepository.countDistinctUsersRegistered(month, dashBoardRequest.yearGetUserJoinAuction());
            totalParticipationByMonth[month - 1] = participation.doubleValue();
        }

        // Calculate revenue for the last 10 years
        Double[] totalRevenueNear10Year = new Double[10];
        for (int i = 0; i < 10; i++) {
            int year = Year.now().getValue() - 9 + i;
            Double totalRevenueForYear = transactionRepository.getTotalCommissionRevenueByYear(year);
            totalRevenueForYear = (totalRevenueForYear != null) ? totalRevenueForYear : 0.0;

            Double totalRegistrationFeeRevenueForYear = transactionRepository.getTotalRegistrationFeeRevenueByYear(year);
            totalRegistrationFeeRevenueForYear = (totalRegistrationFeeRevenueForYear != null) ? totalRegistrationFeeRevenueForYear : 0.0;

            totalRevenueNear10Year[i] = totalRevenueForYear + totalRegistrationFeeRevenueForYear;
        }

        return DashBoardResponse.builder()
                .totalUser(totalUser)
                .totalRevenueToday(totalRevenueToday + totalRegistrationFeeRevenueToday)
                .totalJewelryPricing(totalJewelryPricing)
                .totalJewelryPriced(totalJewelryPriced)
                .totalJewelryNotHasAuction(totalJewelryNotHasAuction)
                .totalJewelryHasAuction(totalJewelryHasAuction)
                .totalJewelryHandover(totalJewelryHandover)
                .totalUsersVerified(totalUsersVerified)
                .totalUsersActive(totalUsersActive)
                .totalUsersInActive(totalUsersInActive)
                .totalMembers(totalMembers)
                .totalStaffs(totalStaffs)
                .totalManagers(totalManagers)
                .totalAdmins(totalAdmins)
                .totalUsersByMonth(totalUsersRegisterByMonth)
                .totalAuctionByMonth(totalAuctionByMonth)
                .auctionFailed(auctionFailed)
                .auctionSuccess(auctionSuccess)
                .totalParticipationByMonth(totalParticipationByMonth)
                .totalRevenueNear10Year(totalRevenueNear10Year)
                .totalRevenueByMonth(totalRevenueByMonth)
                .build();
    }
}
