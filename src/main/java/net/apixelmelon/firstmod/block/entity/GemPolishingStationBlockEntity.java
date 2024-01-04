package net.apixelmelon.firstmod.block.entity;

import net.apixelmelon.firstmod.block.custom.GemPolishingStationBlock;
import net.apixelmelon.firstmod.item.ModItems;
import net.apixelmelon.firstmod.recipe.GemPolishingRecipe;
import net.apixelmelon.firstmod.screen.GemPolishingStationMenu;
import net.apixelmelon.firstmod.util.InventoryDirectionEntry;
import net.apixelmelon.firstmod.util.InventoryDirectionWrapper;
import net.apixelmelon.firstmod.util.ModEnergyStorage;
import net.apixelmelon.firstmod.util.WrappedHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class GemPolishingStationBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        } // Updates the block entity each time the contents are changed/updated

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0 -> true;
                case 1 -> stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
                case 2 -> false;
                case 3 -> stack.getItem() == ModItems.CORN.get();
                default -> super.isItemValid(slot, stack);
            }; // Only allows valid items to be inserted
        }
    };

    private static final int INPUT_SLOT = 0;
    private static final int FLUID_INPUT_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;
    private static final int ENERGY_ITEM_SLOT = 3;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            new InventoryDirectionWrapper(itemHandler,
                    new InventoryDirectionEntry(Direction.DOWN, OUTPUT_SLOT, false),
                    new InventoryDirectionEntry(Direction.NORTH, INPUT_SLOT, true),
                    new InventoryDirectionEntry(Direction.SOUTH, OUTPUT_SLOT, false),
                    new InventoryDirectionEntry(Direction.EAST, OUTPUT_SLOT, false),
                    new InventoryDirectionEntry(Direction.WEST, INPUT_SLOT, true),
                    new InventoryDirectionEntry(Direction.UP, INPUT_SLOT, true)).directionsMap;
    // This is where to customise where the inputs and outputs of the block entity are

    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;
    private final int DEFAULT_MAX_PROGRESS = 78;

    private int energyAmount = 0;
    private final int DEFAULT_ENERGY_AMOUNT = 100;

    private FluidStack neededFluidStack = FluidStack.EMPTY;

    private final ModEnergyStorage ENERGY_STORAGE = createEnergyStorage();
    private final FluidTank FLUID_TANK = createFluidTank();

    private FluidTank createFluidTank() {
        return new FluidTank(64000) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                if(!level.isClientSide()) {
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                }
            }

            @Override
            public boolean isFluidValid(FluidStack stack) {
                return true;
            } // Only fluids specified here will be valid
        };
    }

    private ModEnergyStorage createEnergyStorage() {
        return new ModEnergyStorage(64000, 200) {
            @Override
            public void onEnergyChanged() {
                setChanged();
                getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        };
    } // Ensures energy is synced between client and server

    public GemPolishingStationBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GEM_POLISHING_BE.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> GemPolishingStationBlockEntity.this.progress;
                    case 1 -> GemPolishingStationBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> GemPolishingStationBlockEntity.this.progress = pValue;
                    case 1 -> GemPolishingStationBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public IEnergyStorage getEnergyStorage() {
        return this.ENERGY_STORAGE;
    }

    public FluidStack getFluid() {
        return FLUID_TANK.getFluid();
    }

    public ItemStack getRenderStack() {
        ItemStack stack = itemHandler.getStackInSlot(OUTPUT_SLOT);

        if(stack.isEmpty()) {
            stack = itemHandler.getStackInSlot(INPUT_SLOT);
        }

        return stack;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }

        if(cap == ForgeCapabilities.FLUID_HANDLER) {
            return lazyFluidHandler.cast();
        }

        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            if(side == null) {
                return lazyItemHandler.cast();
            }

            if(directionWrappedHandlerMap.containsKey(side)) {
                Direction localDir = this.getBlockState().getValue(GemPolishingStationBlock.FACING);

                if(side == Direction.DOWN ||side == Direction.UP) {
                    return directionWrappedHandlerMap.get(side).cast();
                }

                return switch (localDir) {
                    default -> directionWrappedHandlerMap.get(side.getOpposite()).cast();
                    case EAST -> directionWrappedHandlerMap.get(side.getClockWise()).cast();
                    case SOUTH -> directionWrappedHandlerMap.get(side).cast();
                    case WEST -> directionWrappedHandlerMap.get(side.getCounterClockWise()).cast();
                };
            }
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
        lazyFluidHandler = LazyOptional.of(() -> FLUID_TANK);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
        lazyFluidHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.firstmod.gem_polishing_station");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new GemPolishingStationMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("gem_polishing_station.progress", progress);
        pTag.putInt("gem_polishing_station.max_progress", maxProgress);
        pTag.putInt("gem_polishing_station.energy_amount", energyAmount);
        neededFluidStack.writeToNBT(pTag);

        pTag.putInt("energy", ENERGY_STORAGE.getEnergyStored());
        pTag = FLUID_TANK.writeToNBT(pTag);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("gem_polishing_station.progress");
        maxProgress = pTag.getInt("gem_polishing_station.max_progress");
        energyAmount = pTag.getInt("gem_polishing_station.energy_amount");
        neededFluidStack = FluidStack.loadFluidStackFromNBT(pTag);
        ENERGY_STORAGE.setEnergy(pTag.getInt("energy"));
        FLUID_TANK.readFromNBT(pTag);
    } // Loads the saved data when the entity is loaded

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        fillUpOnEnergy();
        fillUpOnFluid();

        if (isOutputSlotEmptyOrReceivable() && hasRecipe()) {
            increaseCraftingProgress();
            extractEnergy();
            setChanged(pLevel, pPos, pState);

            if (hasProgressFinished()) {
                craftItem();
                extractFluid();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    } // Called once for every tick on the server

    private void extractFluid() {
        this.FLUID_TANK.drain(neededFluidStack.getAmount(), IFluidHandler.FluidAction.EXECUTE);
    }

    private void fillUpOnFluid() {
        if(hasFluidSourceInSlot(FLUID_INPUT_SLOT)) {
            transferItemFluidToTank(FLUID_INPUT_SLOT);
        }
    }

    private void transferItemFluidToTank(int fluidInputSlot) {
        this.itemHandler.getStackInSlot(fluidInputSlot).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(iFluidHandlerItem -> {
            int drainAmount = Math.min(this.FLUID_TANK.getSpace(), 1000);

            FluidStack stack = iFluidHandlerItem.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
            fillTankWithFluid(stack, iFluidHandlerItem.getContainer());
        });
    }

    private void fillTankWithFluid(FluidStack stack, ItemStack container) {
        this.FLUID_TANK.fill(new FluidStack(stack.getFluid(), stack.getAmount()), IFluidHandler.FluidAction.EXECUTE);

        this.itemHandler.extractItem(FLUID_INPUT_SLOT, 1, false);
        this.itemHandler.insertItem(FLUID_INPUT_SLOT, container, false);
    }

    private boolean hasFluidSourceInSlot(int fluidInputSlot) {
        return this.itemHandler.getStackInSlot(fluidInputSlot).getCount() > 0 &&
                this.itemHandler.getStackInSlot(fluidInputSlot).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
    }

    private void extractEnergy() {
        this.ENERGY_STORAGE.extractEnergy(energyAmount, false);
    }

    private void fillUpOnEnergy() {
        if(hasEnergyItemInSlot(ENERGY_ITEM_SLOT)) {
            this.ENERGY_STORAGE.receiveEnergy(3200, false);
        }
    }

    private boolean hasEnergyItemInSlot(int energyItemSlot) {
        return !this.itemHandler.getStackInSlot(energyItemSlot).isEmpty() &&
                this.itemHandler.getStackInSlot(energyItemSlot).getItem() == ModItems.CORN.get();
    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ||
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() < this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }

    private void resetProgress() {
        progress = 0;
    }

    private void craftItem() {
        Optional<GemPolishingRecipe> recipe = getCurrentRecipe();
        ItemStack result = recipe.get().getResultItem(null);

        this.itemHandler.extractItem(INPUT_SLOT, 1, false);

        this.itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(result.getItem(),
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
    }

    private boolean hasRecipe() {
        Optional<GemPolishingRecipe> recipe = getCurrentRecipe();

        if(recipe.isEmpty()) {
            return false;
        }

        maxProgress = recipe.get().getCraftTime();
        energyAmount = recipe.get().getEnergyAmount();
        neededFluidStack = recipe.get().getFluidStack();

        ItemStack result = recipe.get().getResultItem(getLevel().registryAccess());

        return canInsertAmountIntoOutputSlot(result.getCount())
                && canInsertItemIntoOutputSlot(result.getItem()) && hasEnoughEnergyToCraft()
                && hasEnoughFluidToCraft();
    }

    private boolean hasEnoughFluidToCraft() {
        return this.FLUID_TANK.getFluidAmount() >= neededFluidStack.getAmount();
    }

    private boolean hasEnoughEnergyToCraft() {
        return this.ENERGY_STORAGE.getEnergyStored() >= energyAmount * maxProgress;
    }

    private Optional<GemPolishingRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());

        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(GemPolishingRecipe.Type.INSTANCE, inventory, level);
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count <= this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }

    private boolean hasProgressFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress() {
        this.progress++;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
    }
}
